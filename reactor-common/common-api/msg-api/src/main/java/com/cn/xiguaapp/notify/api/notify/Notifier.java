package com.cn.xiguaapp.notify.api.notify;

import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.template.Provider;
import com.cn.xiguaapp.notify.api.template.Template;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 通知器,用于发送通知,如: 短信,邮件,语音,微信等s
 *
 * @author xiguaapp
 * @since 1.0
 */
public interface Notifier<T extends Template> {

    /**
     * @return 通知器ID
     */
    Long getNotifierId();

    /**
     * 获取通知类型,如: 语音通知
     *
     * @return 通知类型
     * @see DefaultNotifyType
     */
    @Nonnull
    NotifyType getType();

    /**
     * 获取通知服务提供商,如: aliyun 等
     *
     * @return 通知服务提供商
     */
    @Nonnull
    Provider getProvider();

    /**
     * 指定模版ID进行发送.
     * 发送失败或者模版不存在将返回{@link Mono#error(Throwable)}.
     *
     * @param templateId 模版ID
     * @param context    上下文
     * @return 异步发送结果
     * @see Mono#doOnError(Consumer)
     * @see Mono#doOnSuccess(Consumer)
     * @see Template
     */
    @Nonnull
    Mono<Void> send(@Nonnull String templateId, Map<String,Object> context);

    /**
     * 指定模版{@link Template}并发送.
     * <p>
     * 注意:不同等服务商使用的模版实现不同.
     * <p>
     * 发送失败返回{@link Mono#error(Throwable)}.
     *
     * @param template 模版
     * @param context  上下文
     * @return 异步发送结果
     * @see Mono#doOnError(Consumer)
     * @see Mono#doOnSuccess(Consumer)
     * @see Template
     */
    @Nonnull
    Mono<Void> send(@Nonnull T template, @Nonnull Map<String,Object> context);

    /**
     * 关闭通知器,以释放相关资源
     *
     * @return 关闭结果
     */
    @Nonnull
    Mono<Void> close();

    default <R extends Notifier<T>> R unwrap(Class<R> type) {
        return type.cast(this);
    }
}
