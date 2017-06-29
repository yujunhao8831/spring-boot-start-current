package com.aidijing.service;

import com.aidijing.domain.User;
import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

/**
 * <p>
 * 后台管理用户 服务类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
public interface UserService extends IService<User> {

    /**
     * 默认分页
     * @param pageRowBounds
     *   <pre>
     *      <table border="1">
     *          <caption>参数说明({@link PageRowBounds})</caption>
     *          <tr>
     *              <td>参数名称</td>
     *              <td>参数类型</td>
     *              <td>参数说明</td>
     *          </tr>
     *          <tr>
     *              <td>PageRowBounds#getOffset()</td>
     *              <td>int</td>
     *              <td>页码</td>
     *          </tr>
     *          <tr>
     *              <td>PageRowBounds#getLimit()</td>
     *              <td>int</td>
     *              <td>每页显示数量</td>
     *          </tr>
     *      </table>
     *      <br>
     *     示例 :
     *          <ul>
     *              <li> 1. PageInfo page = listPage(new PageRowBounds(1,10)); </li>
     *              <li> 2. 控制器中直接使用 PageRowBounds 作为参数接收即可,就算客户端不传值也会有默认值. </li>
     *              <li> 默认分页起始值 : {@link com.aidijing.common.GlobalConstant#DEFAULT_PAGE_NUM} </li>
     *              <li> 默认分页大小值 : {@link com.aidijing.common.GlobalConstant#DEFAULT_PAGE_SIZE} </li>
     *          </ul>
     *
     *
     *   </pre>                    
     * @return PageInfo
     */
    PageInfo listPage ( PageRowBounds pageRowBounds );


    List< User > list ();

    User get ( Long id );

    User update ( User User );

    User save ( User User );

    boolean delete ( Long id );

    /**
     * 异步更新
     *
     * @return
     */
    ListenableFuture< Boolean > asyncUpdate ();


    User findByUsername ( String Username );
    
    
    
        
}
