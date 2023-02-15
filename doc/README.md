# xiguaapp-reactor
##介绍就免了 直接从权限开始吧
    用户 组织架构 菜单权限 数据权限(查看权限) 操作权限(新增 删除 编辑)
    
    1 用户：
        1：登陆用户设计
            id(登陆用户id)    
            user_id(用户id) 
            account(登陆账号<电话号码 邮箱 自定义账号 open_id>) 
            password(密码 凭证信息) 
            account_type(登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等) 
            register_ip(注册ip) 
            create_time(创建时间) 
            update_time(更新时间) 
            status(状态：0禁用 1启用 2锁定 3:账号注销(30天后自动删除数据)) 
            is_del(逻辑删除)
        2:系统用户-管理员信息
            id
            nick_name(昵称)
            avatar(头像地址)
            user_name(登陆账号)
            email(邮箱) 
            mobile(电话) 
            open_id(第三方标识id) 
            user_type(用户类型 super超级管理员 normal普通管理员) 
            user_desc(用户描述) 
            create_time(创建时间) 
            update_time(更新时间) 
            status(状态) 
            company_id
            is_del(逻辑删除)
        3:移动用户
            id 
            user_name （账号）
            nick_name （昵称）
            avatar    （头像）
            email     （邮箱）
            mobile    （电话）
            open_id   （第三方凭证ID）
            user_type （用户类型：mobile-移动用户）
            from_type(用户来源：system-系统用户 qq weixin-微信 weibo-微博等)
            user_desc （用户简介）
            create_time （创建时间）
            update_time （修改时间）
            status    （状态：0禁用 1启用 2锁定 3:账号注销(30天后自动删除数据)）
            is_del    （逻辑删除）
            city      （所在城市：结构<xxx省xxx市/州xxx县/区xxx乡/镇>）
            age       （年龄）
            sex       （性别：0男<默认>1女）
            id_card   （身份证）
            card_front_img   （身份证图片正面）
            card_reverse_img （身份证反面）
            real_name （真实姓名）
            is_real   （认证：0未认证 1已认证）
        4：开发平台用户
            id
            user_name   (开发平台账号)
            nick_name   (开放平台昵称)
            merchant_num(商户号)
            avatar     （开放平台头像）
            email      （邮箱）
            mobile     （电话）
            user_type  （用户类型：open-person 个人 open-company 企业）
            user_desc  （开放平台用户简介）
            city       （所在城市）
            id_card    （法人/个人 身份证号码）
            card_front_img   （身份证图片正面）
            card_reverse_img （身份证反面）
            real_name  （开发者真实姓名）
            is_real    （认证：0未认证 1已认证）
            company_name（企业名称）
            company_card（企业标识号码）
            company_img （企业营业执照）
            company_type（企业类型）
            create_time（创建时间）
            update_time（更新时间）
            status     （状态：0禁用 1启用 2锁定 3:账号注销(30天后自动删除数据)）
            is_del     （逻辑删除）
    2 组织架构：
        1：组织
            id
            organization_name 组织名称
            organization_code 组织编码
            organization_desc 组织介绍
            parent_id         上级编码
            create_time
            update_time
            status            状态（0禁用 1启用 2锁定）
        2：身份
            0：身份
                id
                post_name 职务名
                post_code 职务编码
                post_desc 职务介绍
                parent_id 上级职务id
                status    状态 0禁用 1启用 2锁定
            
            1：用户-身份关联
                id
                post_id 职位id
                user_id 用户id
                create_time
                update_time
                is_del
            2：身份-角色关联
                id
                post_id
                role_id
        3：用户组
            2.1 用户组
                id
                group_name 组名称
                group_code 组编码
            2.2 用户组-角色权限
                id
                group_id 用户组id
                role_id  角色id
                system_id系统id
            2.3 用户-用户组
                id
                group_id 用户组id
                user_id  用户id
        
    3：企业
        3.0 企业
            id
            merchant_code 企业编码
            merchant_name 企业名称/公司/企业
            create_time
            update_time
            parent_id 父级编码
            is_del
            status      状态 0禁用 1启用 2锁定
        
        3.1 角色
            id
            role_name 角色名称
            role_code 角色编码
            parent_id 上级角色id
            system_id 系统id
        
            
        3.2 权限
            id 
            authority_name 权限名
            authority_code 权限标识
            parent_id 权限父类
            system_id 系统id
            menu_id   菜单id
            sort      排序
        3.3 角色-权限关联表
            id
            authority_id 权限id
            role_id      角色id
            system_id    系统id
    4：系统资源
        4.1 系统资源-功能操作
            id
            action_code资源编码
            action_name资源名称
            action_desc资源描述
            menu_id    菜单(资源父节点)
            sort       排序
            status     状态 0无限 1有效
            service_name 服务名称
        4.2 系统权限-功能操作关联表
            id
            action_id 操作id
            authority_id 权限ID
    流程图 请看项目
    
            
            