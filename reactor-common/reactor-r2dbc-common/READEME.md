## 0 功能介绍
该core包是所有R2dbc封装(本项目)的结合 提供了R2dbc的增删改查实现(R2dbc和jdbc)
</br>
## 1 优点
1. 节省时间 可以把这部分时间拿去回家陪媳妇
2. 支持R2dbc响应式编程 jdbc响应式且异步非阻塞 摒弃以前的同步阻塞 成功迁移到异步非阻塞的时代潮流中
2. 支持R2dbc/JDBC分页 自动生成id 分布式
3. 依赖性弱 减少sql语句的存在
4. 自定义实体生成数据库、自定义增删改查、多表查询等等
5. 作者定时更新 便于维护
## 2 如何使用?
使用过程中只需引入reactor-r2dbc-common-core包即可
~~~
<dependency>
    <groupId>com.cn.xiguaapp</groupId>
    <artifactId>reactor-r2dbc-common-core</artifactId>
    <version>3.1.1-RED-RELEASE</version>
</dependency>
~~~
======================
## 3 使用案例
### 3.1 ReactiveRepository ReactiveCrudService
### 1.0 ReactiveRepository使用
#### 1.0.1 删除
~~~
    public interface ReactiveDeleteFunction<E,K> {
    ReactiveRepository<E,K>getRepository();

    @Bean
    @Operation(summary = "根据ID删除")
    default Function<Mono<K>, Mono<E>>delete(){
        return id->getRepository()
                .findById(id)
                .switchIfEmpty(Mono.error(NotFoundException::new))
                .flatMap(e->getRepository()
                        .deleteById(id)
                        .thenReturn(e));
    }
}
~~~
#### 1.0.2 查询
~~~
public interface ReactiveQueryFunction<E, K> {
    ReactiveRepository<E, K> getRepository();

    /**
     * 查询 不返回分页结果
     * <pre>GET /_query/no-paging?pageIndex=0&pageSize=20&where=name is 张三&orderBy=id desc</pre>
     *
     * @return 结果流
     * @see QueryParamEntity 查询条件
     */
    @Bean
    @QueryOperation(summary = "使用get方式分页动态查询(不返回总数)", description = "此操作不返回分页总数，如果需要获取全部数据 请设置参数paging=false")
    @ApiOperation(value = "此操作不返回分页总数，如果需要获取全部数据 请设置参数paging=false", notes = "query", response = QueryParamEntity.class)
    default Function<QueryParamEntity, Flux<E>> queryGet() {
        return this::query;
    }

    /**
     * POST方式查询.不返回分页结果
     *
     * <pre>
     *     POST /_query/no-paging
     *
     *     {
     *         "pageIndex":0,
     *         "pageSize":20,
     *         "where":"name like 张%", //放心使用,没有SQL注入
     *         "orderBy":"id desc",
     *         "terms":[ //高级条件
     *             {
     *                 "column":"name",
     *                 "termType":"like",
     *                 "value":"张%"
     *             }
     *         ]
     *     }
     * </pre>
     *
     * @return 结果流
     * @see QueryParamEntity 查询条件
     */
    @Bean
    @Operation(summary = "使用POST方式分页动态查询(不返回总数)",
            description = "此操作不返回分页总数,如果需要获取全部数据,请设置参数paging=false")
    default Function<Mono<QueryParamEntity>, Flux<E>> quertPost() {
        return query -> query.flatMapMany(this::query);
    }

    /**
     * GET方式分页查询
     *
     * <pre>
     *    GET /_query/no-paging?pageIndex=0&pageSize=20&where=name is 张三&orderBy=id desc
     * </pre>
     *
     * @return 分页查询结果
     * @see PagerResult 查询条件
     */
    @Bean
    @QueryOperation(summary = "使用GET方式分页动态查询")
    default Function<QueryParamEntity, Mono<PagerResult<E>>> queryPagerGet() {
        return this::queryPager;
    }

    @Operation(summary = "使用POST方式分页动态查询")
    @SuppressWarnings("all")
    @Bean
    default Function<Mono<QueryParamEntity>,Mono<PagerResult<E>>>queryPagerPost() {
        return query->query.flatMap(this::queryPager);
    }
    /**
     * 统计查询
     *
     * <pre>
     *     GET /_count
     * </pre>
     *
     * @see QueryParamEntity  查询条件
     * @return 统计结果
     */
    @QueryOperation(summary = "使用GET方式查询总数")
    @Bean
    default Function<QueryParamEntity,Mono<Integer>>countGet(){
        return this::count;
    }
    @Bean
    @Operation(summary = "使用POST方式查询总数")
    default Function<Mono<QueryParamEntity>,Mono<Integer>> count() {
        return query->query.flatMap(this::count);
    }

    @Operation(summary = "根据ID查询")
    @Bean
    default Function<Mono<K>,Mono<E>>getById(){
        return id->getRepository()
                .findById(id)
                .switchIfEmpty(Mono.error(NotFoundException::new));
    }


    default Flux<E> query(QueryParamEntity queryParamEntity) {
        return getRepository().createQuery().setParam(queryParamEntity).fetch();
    }

    default Mono<PagerResult<E>> queryPager(QueryParamEntity query) {
        if (query.getTotal() != null) {
            return getRepository()
                    .createQuery()
                    .setParam(query.rePaging(query.getTotal()))
                    .fetch()
                    .collectList()
                    .map(list -> PagerResult.of(query.getTotal(), list, query));
        }

        return Mono.zip(
                getRepository().createQuery().setParam(query).count(),
                query(query.clone()).collectList(),
                (total, data) -> PagerResult.of(total, data, query)
        );
    }

    default Mono<Integer>count(QueryParamEntity queryParamEntity){
        return getRepository().createQuery().setParam(queryParamEntity).count();
    }
}
~~~
#### 1.0.3 新增和编辑
~~~
public interface ReactiveSaveFunction<E, K> {

    ReactiveRepository<E, K> getRepository();

    default E applyCreationEntity(Authentication authentication, E entity) {
        RecordCreationEntity creationEntity = ((RecordCreationEntity) entity);
        creationEntity.setCreateTimeNow();
        creationEntity.setCreatorId(authentication.getUser().getId());
        creationEntity.setCreatorName(authentication.getUser().getName());
        return entity;
    }

    default E applyModifierEntity(Authentication authentication, E entity) {
        RecordModifierEntity modifierEntity = ((RecordModifierEntity) entity);
        modifierEntity.setModifyTimeNow();
        modifierEntity.setModifierId(authentication.getUser().getId());
        modifierEntity.setModifierName(authentication.getUser().getName());
        return entity;
    }

    default E applyAuthentication(E entity, Authentication authentication) {
        if (entity instanceof RecordCreationEntity) {
            entity = applyCreationEntity(authentication, entity);
        }
        if (entity instanceof RecordModifierEntity) {
            entity = applyModifierEntity(authentication, entity);
        }
        return entity;
    }

    @Operation(summary = "保存数据", description = "如果传入了id,并且对应数据存在,则尝试覆盖,不存在则新增.")
    default Mono<SaveResult> save(@RequestBody Flux<E> payload) {
        return Authentication.currentReactive()
                .flatMapMany(auth -> payload.map(entity -> applyAuthentication(entity, auth)))
                .switchIfEmpty(payload)
                .as(getRepository()::save);
    }

    @Operation(summary = "批量新增数据")
    default Mono<Integer> add(@RequestBody Flux<E> payload) {

        return Authentication.currentReactive()
                .flatMapMany(auth -> payload.map(entity -> applyAuthentication(entity, auth)))
                .switchIfEmpty(payload)
                .collectList()
                .as(getRepository()::insertBatch);
    }

    @Operation(summary = "新增单个数据,并返回新增后的数据.")
    default Mono<E> add(@RequestBody Mono<E> payload) {
        return Authentication.currentReactive()
                .flatMap(auth -> payload.map(entity -> applyAuthentication(entity, auth)))
                .switchIfEmpty(payload)
                .flatMap(entity -> getRepository().insert(Mono.just(entity)).thenReturn(entity));
    }


    @Operation(summary = "根据ID修改数据")
    default Mono<Boolean> update(@PathVariable K id, @RequestBody Mono<E> payload) {

        return Authentication.currentReactive()
                .flatMap(auth -> payload.map(entity -> applyAuthentication(entity, auth)))
                .switchIfEmpty(payload)
                .flatMap(entity -> getRepository().updateById(id, Mono.just(entity)))
                .thenReturn(true);

    }
}
~~~
### 1.1 ReactiveCrudService使用
#### 1.0.1 删除
~~~
public interface ReactiveServiceDeleteFunction<E,K> {
    ReactiveCrudService<E,K>getService();
    @Operation(summary = "根据ID删除")
    default Function<Mono<K>,Mono<E>>delete(){
        return id->getService().findById(id).switchIfEmpty(Mono.error(NotFoundException::new))
                .flatMap(e -> getService()
                        .deleteById(id)
                        .thenReturn(e));
    }
}
~~~
#### 1.1.2 查询
~~~
public interface ReactiveServiceQueryFunction<E,K> {
    ReactiveCrudService<E,K>getService();

    /**
     * 查询 不返回分页结果
     * <pre>GET /_query/no-paging?pageIndex=0&pageSize=20&where=name is 张三&orderBy=id desc</pre>
     *
     * @return 结果流
     * @see QueryParamEntity 查询条件
     */
    @Bean
    @QueryOperation(summary = "使用get方式分页动态查询(不返回总数)", description = "此操作不返回分页总数，如果需要获取全部数据 请设置参数paging=false")
    @ApiOperation(value = "此操作不返回分页总数，如果需要获取全部数据 请设置参数paging=false", notes = "query", response = QueryParamEntity.class)
    default Function<QueryParamEntity, Flux<E>> queryGet() {
        return this::query;
    }

    /**
     * POST方式查询.不返回分页结果
     *
     * <pre>
     *     POST /_query/no-paging
     *
     *     {
     *         "pageIndex":0,
     *         "pageSize":20,
     *         "where":"name like 张%", //放心使用,没有SQL注入
     *         "orderBy":"id desc",
     *         "terms":[ //高级条件
     *             {
     *                 "column":"name",
     *                 "termType":"like",
     *                 "value":"张%"
     *             }
     *         ]
     *     }
     * </pre>
     *
     * @return 结果流
     * @see QueryParamEntity 查询条件
     */
    @Bean
    @Operation(summary = "使用POST方式分页动态查询(不返回总数)",
            description = "此操作不返回分页总数,如果需要获取全部数据,请设置参数paging=false")
    default Function<Mono<QueryParamEntity>, Flux<E>> quertPost() {
        return query -> query.flatMapMany(this::query);
    }

    /**
     * GET方式分页查询
     *
     * <pre>
     *    GET /_query/no-paging?pageIndex=0&pageSize=20&where=name is 张三&orderBy=id desc
     * </pre>
     *
     * @return 分页查询结果
     * @see PagerResult 查询条件
     */
    @Bean
    @QueryOperation(summary = "使用GET方式分页动态查询")
    default Function<QueryParamEntity, Mono<PagerResult<E>>> queryPagerGet() {
        return this::queryPager;
    }

    @Operation(summary = "使用POST方式分页动态查询")
    @SuppressWarnings("all")
    @Bean
    default Function<Mono<QueryParamEntity>,Mono<PagerResult<E>>>queryPagerPost() {
        return query->query.flatMap(this::queryPager);
    }
    /**
     * 统计查询
     *
     * <pre>
     *     GET /_count
     * </pre>
     *
     * @see QueryParamEntity  查询条件
     * @return 统计结果
     */
    @QueryOperation(summary = "使用GET方式查询总数")
    @Bean
    default Function<QueryParamEntity,Mono<Integer>>countGet(){
        return this::count;
    }
    @Bean
    @Operation(summary = "使用POST方式查询总数")
    default Function<Mono<QueryParamEntity>,Mono<Integer>> count() {
        return query->query.flatMap(this::count);
    }

    @Operation(summary = "根据ID查询")
    @Bean
    default Function<Mono<K>,Mono<E>>getById(){
        return id->getService()
                .findById(id)
                .switchIfEmpty(Mono.error(NotFoundException::new));
    }


    default Flux<E> query(QueryParamEntity queryParamEntity) {
        return getService().createQuery().setParam(queryParamEntity).fetch();
    }

    default Mono<PagerResult<E>> queryPager(QueryParamEntity query) {
        if (query.getTotal() != null) {
            return getService()
                    .createQuery()
                    .setParam(query.rePaging(query.getTotal()))
                    .fetch()
                    .collectList()
                    .map(list -> PagerResult.of(query.getTotal(), list, query));
        }

        return Mono.zip(
                getService().createQuery().setParam(query).count(),
                query(query.clone()).collectList(),
                (total, data) -> PagerResult.of(total, data, query)
        );
    }

    default Mono<Integer>count(QueryParamEntity queryParamEntity){
        return getService().createQuery().setParam(queryParamEntity).count();
    }
}
~~~
#### 1.1.3 新增和编辑
~~~
public interface ReactiveServiceSaveController<E,K>  {

    ReactiveCrudService<E,K> getService();

    default E applyCreationEntity(Authentication authentication, E entity) {
        RecordCreationEntity creationEntity = ((RecordCreationEntity) entity);
        creationEntity.setCreateTimeNow();
        creationEntity.setCreatorId(authentication.getUser().getId());
        creationEntity.setCreatorName(authentication.getUser().getName());
        return entity;
    }

    default E applyModifierEntity(Authentication authentication, E entity) {
        RecordModifierEntity modifierEntity = ((RecordModifierEntity) entity);
        modifierEntity.setModifyTimeNow();
        modifierEntity.setModifierId(authentication.getUser().getId());
        modifierEntity.setModifierName(authentication.getUser().getName());
        return entity;
    }

    default E applyAuthentication(E entity, Authentication authentication) {
        if (entity instanceof RecordCreationEntity) {
            entity = applyCreationEntity(authentication, entity);
        }
        if (entity instanceof RecordModifierEntity) {
            entity = applyModifierEntity(authentication, entity);
        }
        return entity;
    }

    @Operation(summary = "保存数据", description = "如果传入了id,并且对应数据存在,则尝试覆盖,不存在则新增.")
    default Mono<SaveResult> save(@RequestBody Flux<E> payload) {
        return Authentication.currentReactive()
                .flatMapMany(auth -> payload.map(entity -> applyAuthentication(entity, auth)))
                .switchIfEmpty(payload)
                .as(getService()::save);
    }

    @Operation(summary = "批量新增数据")
    default Mono<Integer> add(@RequestBody Flux<E> payload) {

        return Authentication.currentReactive()
                .flatMapMany(auth -> payload.map(entity -> applyAuthentication(entity, auth)))
                .switchIfEmpty(payload)
                .collectList()
                .as(getService()::insertBatch);
    }

    @Operation(summary = "新增单个数据,并返回新增后的数据.")
    default Mono<E> add(@RequestBody Mono<E> payload) {
        return Authentication.currentReactive()
                .flatMap(auth -> payload.map(entity -> applyAuthentication(entity, auth)))
                .switchIfEmpty(payload)
                .flatMap(entity -> getService().insert(Mono.just(entity)).thenReturn(entity));
    }


    @Operation(summary = "根据ID修改数据")
    default Mono<Boolean> update(@PathVariable K id, @RequestBody Mono<E> payload) {

        return Authentication.currentReactive()
                .flatMap(auth -> payload.map(entity -> applyAuthentication(entity, auth)))
                .switchIfEmpty(payload)
                .flatMap(entity -> getService().updateById(id, Mono.just(entity)))
                .thenReturn(true);

    }
~~~
###偷懒技巧
如若不想编写 只需引入以下案例即可
~~~
ReactiveCrudFunction
ReactiveDeleteFunction
ReactiveQueryFunction
ReactiveServiceCrudFunction
ReactiveServiceDeleteFunction
ReactiveServiceQueryFunction
ReactiveTreeServiceQueryFunction
~~~
好多朋友肯定想问为什么不把新增编辑做了？
因为在使用api过程中我们需要获取创建人和更新人的基本信息 所以新增编辑留给各位兄弟自己做了

###最后
感谢我媳妇儿、我的家人的不断支持和陪伴 感谢知秋大佬提供的书籍【java编程方法论-响应式spring ractor3设计与实现】和技术支持