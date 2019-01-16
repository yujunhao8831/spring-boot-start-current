package com.goblin.manage.service;

import com.goblin.common.PagingRequest;
import com.goblin.manage.bean.domain.MailSubscribe;
import com.baomidou.mybatisplus.extension.service.IService;
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
