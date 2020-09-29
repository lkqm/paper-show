package com.github.lkqm.paper.util

/**
 * AssertUtils
 *
 * @author Mario Luo
 * @date 2019.01.19 11:34
 *
 * @throws ServiceException 参数校验失败抛出异常
 */
object AssertUtils {
    /**
     * 参数校验
     * @param expression 期望值
     * @param message 不满足期望值的消息
     */
    fun checkIsTrue(expression: Boolean, message: String?, vararg msgParams: Any?) {
        if (!expression) {
            throw ServiceException(message)
        }
    }

    fun checkIsNotNull(obj: Any?, message: String?, vararg msgParams: Any?) {
        if (obj == null) {
            throw ServiceException(message)
        }
    }
}