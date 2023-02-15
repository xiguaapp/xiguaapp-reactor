package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import com.cn.xiguapp.common.core.gen.IDGenerator;
import com.cn.xiguapp.common.core.gen.RandomUtil;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiguaapp
 * @desc 树形支持
 * @since 1.0 22:35
 */
public interface TreeSupportEntity<S> extends Entity {
    S getId();

    void setId(S id);

    String getPath();

    void setPath(String path);

    S getParentId();

    void setParentId(S parentId);

    Integer getLevel();

    void setLevel(Integer level);

    <T extends TreeSupportEntity<S>> List<T> getChildren();

    /**
     * 根据path获取父节点的path
     *
     * @param path path
     * @return 父节点path
     */
    static String getParentPath(String path) {
        if (path == null || path.length() < 4) {
            return null;
        }
        return path.substring(0, path.length() - 5);
    }

    static <T extends TreeSupportEntity> void forEach(Collection<T> list, Consumer<T> consumer) {
        Queue<T> queue = new LinkedList<>(list);
        Set<Long> all = new HashSet<>();
        for (T node = queue.poll(); node != null; node = queue.poll()) {
            long hash = System.identityHashCode(node);
            if (all.contains(hash)) {
                continue;
            }
            all.add(hash);
            consumer.accept(node);
            if (!CollectionUtils.isEmpty(node.getChildren())) {
                queue.addAll(node.getChildren());
            }
        }
    }

    static <T extends TreeSupportEntity<S>, S> List<T> expandTree2List(T parent, IDGenerator<S> idGenerator) {
        List<T> list = new LinkedList<>();
        expandTree2List(parent, list, idGenerator);

        return list;
    }

    static <T extends TreeSupportEntity<S>, S> void expandTree2List(T parent, List<T> target, IDGenerator<S> idGenerator) {
        expandTree2List(parent, target, idGenerator, null);
    }


    /**
     * 将树形结构转为列表结构，并填充对应的数据。<br>
     * 如树结构数据： {name:'父节点',children:[{name:'子节点1'},{name:'子节点2'}]}<br>
     * 解析后:[{id:'id1',name:'父节点',path:'<b>aoSt</b>'},{id:'id2',name:'子节点1',path:'<b>aoSt</b>-oS5a'},{id:'id3',name:'子节点2',path:'<b>aoSt</b>-uGpM'}]
     *
     * @param root        树结构的根节点
     * @param target      目标集合,转换后的数据将直接添加({@link List#add(Object)})到这个集合.
     * @param <T>         继承{@link TreeSupportEntity}的类型
     * @param idGenerator ID生成策略
     * @param <S>        主键类型
     */
    static <T extends TreeSupportEntity<S>, S> void expandTree2List(T root, List<T> target, IDGenerator<S> idGenerator, BiConsumer<T, List<T>> childConsumer) {
        //尝试设置树路径path
        if (root.getPath() == null) {
            root.setPath(RandomUtil.randomChar(4));
        }
        if (root.getPath() != null) {
            root.setLevel(root.getPath().split("[-]").length);
        }
        //尝试设置排序
        if (root instanceof SortSupportEntity) {
            SortSupportEntity sortableRoot = ((SortSupportEntity) root);
            Long index = sortableRoot.getSortIndex();
            if (null == index) {
                sortableRoot.setSortIndex(1L);
            }
        }

        if (CollectionUtils.isEmpty(root.getChildren())) {
            target.add(root);
            return;
        }

        //尝试设置id
        S parentId = root.getId();
        if (parentId == null) {
            parentId = idGenerator.generate();
            root.setId(parentId);
        }

        //所有节点处理队列
        Queue<T> queue = new LinkedList<>();
        queue.add(root);
        //已经处理过的节点过滤器
        Set<Long> filter = new HashSet<>();

        for (T parent = queue.poll(); parent != null; parent = queue.poll()) {
            long hash = System.identityHashCode(parent);
            if (filter.contains(hash)) {
                continue;
            }
            filter.add(hash);

            //处理子节点
            if (!CollectionUtils.isEmpty(parent.getChildren())) {
                long index = 1;
                for (TreeSupportEntity<S> child : parent.getChildren()) {
                    if (child.getId() == null) {
                        child.setId(idGenerator.generate());
                    }
                    child.setParentId(parent.getId());
                    child.setPath(parent.getPath() + "-" + RandomUtil.randomChar(4));
                    child.setLevel(child.getPath().split("[-]").length);

                    //子节点排序
                    if (child instanceof SortSupportEntity && parent instanceof SortSupportEntity) {
                        SortSupportEntity sortableParent = ((SortSupportEntity) parent);
                        SortSupportEntity sortableChild = ((SortSupportEntity) child);
                        sortableChild.setSortIndex(sortableParent.getSortIndex() * 100 + index++);
                    }
                    queue.add((T) child);
                }
            }
            if (childConsumer != null) {
                childConsumer.accept(parent, new ArrayList<>());
            }
            target.add(parent);
        }
    }

    /**
     * 集合转为树形结构,返回根节点集合
     *
     * @param dataList      需要转换的集合
     * @param childConsumer 设置子节点回调
     * @param <N>           树节点类型
     * @param <S>          主键类型
     * @return 树形结构集合
     */
    static <N extends TreeSupportEntity<S>, S> List<N> list2tree(Collection<N> dataList, BiConsumer<N, List<N>> childConsumer) {
        return list2tree(dataList, childConsumer, (Function<TreeHelper<N, S>, Predicate<N>>) predicate -> node -> node == null || predicate.getNode(node.getParentId()) == null);
    }

    static <N extends TreeSupportEntity<S>, S> List<N> list2tree(Collection<N> dataList,
                                                                   BiConsumer<N, List<N>> childConsumer,
                                                                   Predicate<N> rootNodePredicate) {
        return list2tree(dataList, childConsumer, (Function<TreeHelper<N, S>, Predicate<N>>) predicate -> rootNodePredicate);
    }

    /**
     * 列表结构转为树结构,并返回根节点集合
     *
     * @param dataList          数据集合
     * @param childConsumer     子节点消费接口,用于设置子节点
     * @param predicateFunction 根节点判断函数,传入helper,获取一个判断是否为跟节点的函数
     * @param <N>               元素类型
     * @param <S>              主键类型
     * @return 根节点集合
     */
    static <N extends TreeSupportEntity<S>, S> List<N> list2tree(final Collection<N> dataList,
                                                                   final BiConsumer<N, List<N>> childConsumer,
                                                                   final Function<TreeHelper<N, S>, Predicate<N>> predicateFunction) {
        Objects.requireNonNull(dataList, "source list can not be null");
        Objects.requireNonNull(childConsumer, "child consumer can not be null");
        Objects.requireNonNull(predicateFunction, "root predicate function can not be null");

        Supplier<Stream<N>> streamSupplier = () -> dataList.stream();
        // id,node
        Map<S, N> cache = new HashMap<>();
        // parentId,children
        Map<S, List<N>> treeCache = streamSupplier.get()
                .peek(node -> cache.put(node.getId(), node))
                .filter(e -> e.getParentId() != null)
                .collect(Collectors.groupingBy(TreeSupportEntity::getParentId));

        Predicate<N> rootNodePredicate = predicateFunction.apply(new TreeHelper<N, S>() {
            @Override
            public List<N> getChildren(S parentId) {
                return treeCache.get(parentId);
            }

            @Override
            public N getNode(S id) {
                return cache.get(id);
            }
        });

        return streamSupplier.get()
                //设置每个节点的子节点
                .peek(node -> childConsumer.accept(node, treeCache.get(node.getId())))
                //获取根节点
                .filter(rootNodePredicate)
                .collect(Collectors.toList());
    }

    /**
     * 树结构Helper
     *
     * @param <T>  节点类型
     * @param <S> 主键类型
     */
    interface TreeHelper<T, S> {
        /**
         * 根据主键获取子节点
         *
         * @param parentId 节点ID
         * @return 子节点集合
         */
        List<T> getChildren(S parentId);

        /**
         * 根据id获取节点
         *
         * @param id 节点ID
         * @return 节点
         */
        T getNode(S id);
    }
}
