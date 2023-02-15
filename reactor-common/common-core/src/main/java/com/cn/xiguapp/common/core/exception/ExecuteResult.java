/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午6:40 >
 *
 *       Send: 1125698980@qq.com
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.cn.xiguapp.common.core.exception;

import javax.script.ScriptException;
import java.util.function.Supplier;

public class ExecuteResult {
    private boolean success;

    private Object result;

    private String message;

    private transient Exception exception;

    private long useTime;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * use {@link this#get()} or {@link this#getIfSuccess()}
     *
     * @return
     */
    @Deprecated
    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessage() {
        if (message == null && exception != null) {
            message = exception.getMessage();
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getResult());
    }

    public Object get() {
        return result;
    }

    public Object getIfSuccess() throws Exception {
        if (!success) {
            if (exception != null) {
                throw exception;
            } else
                throw new ScriptException(message);
        }
        return result;
    }

    public Object getIfSuccess(Object defaultValue) {
        if (!success) {
            return defaultValue;
        }
        return result;
    }

    public Object getIfSuccess(Supplier<?> supplier) {
        if (!success) {
            return supplier.get();
        }
        return result;
    }

}
