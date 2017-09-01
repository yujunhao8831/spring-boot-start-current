package com.aidijing.manage.service;

import com.aidijing.common.PagingRequest;
import com.aidijing.manage.bean.domain.MailSubscribe;
import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 邮件订阅 服务类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-08-07
 */
public interface MailSubscribeService extends IService<MailSubscribe> {


    PageInfo<MailSubscribe> listPage ( PagingRequest pagingRequest );






}
