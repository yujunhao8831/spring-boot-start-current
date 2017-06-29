package com.aidijing.service;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.plugins.Page;
import com.aidijing.common.util.GenerationCode;
import com.aidijing.domain.UserActionHistory;
import com.aidijing.domain.enums.ActionLevel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith( SpringRunner.class )
@SpringBootTest
public class UserActionHistoryServiceTest {

    @Autowired
    private UserActionHistoryService userActionHistoryService;

    

    @Test
    public void userActionHistoryServiceTest () throws Exception {
        // # insert 
        // ## 1. 选择插入,如果某个字段为空,那么这个字段则不会进插入
        userActionHistoryService.insert(
                new UserActionHistory()
                        .setUserId( 1L )
                        .setActionLevel( ActionLevel.ERROR )
        );
        
        // ## 2. 批量插入
        List< UserActionHistory > histories = new ArrayList<>( 50 );
        for ( int i = 0 ; i < 50 ; i++ ) {
            histories.add(
                    new UserActionHistory()
                            .setUserId( 1L )
                            .setActionLevel( ActionLevel.ERROR )

            );
        }
        userActionHistoryService.insertBatch( histories );
        // # select
        // ## 1. 根据主键查询
        UserActionHistory history = userActionHistoryService.selectById(
                histories.get( 0 ).getId()
        );
        // ## 2. 根据主键批量查询
        userActionHistoryService.selectBatchIds(
                histories.parallelStream()
                         .map( UserActionHistory::getId )
                         .collect( Collectors.toList() )
        );
        // ## 3. 统计 + 模糊查询等 
        userActionHistoryService.selectCount(
                new Condition()
                        .like( " action_level = {0} ", ActionLevel.ERROR.getCode() )
        );
        
        // ## 4. and 
        userActionHistoryService.selectMap(
                new Condition()
                        .like( " action_level = {0} ", ActionLevel.ERROR.getCode() )
                        .and( "user_id = {0}", 1L )
        );

        // ## 5. 分页查询 
        userActionHistoryService.selectPage(
                new Page<>( 1, 10 )
        );
        
        // ... ... 

        // # update
        // ## 1. 选择性更新
        userActionHistoryService.updateById( history );
        // ## 2. 选择性批量更新
        userActionHistoryService.updateBatchById(
                histories.parallelStream()
                         .filter( userActionHistory -> userActionHistory.setActionLog( GenerationCode.globalUniqueId() ) != null )
                         .collect( Collectors.toList() )
        );
        // ## 3. 选择性 + 根据条件更新
        userActionHistoryService.update(
                history.setUserRealName( "披荆斩棘" ),
                new Condition()
                        .eq( "id", history.getId() )
                        .and( " action_level = {0} ", ActionLevel.ERROR.getCode() )
        );

        // # delete
        // ## 1. 删除
        userActionHistoryService.deleteById( 1L );
        // ## 2. 根据条件删除
        userActionHistoryService.delete(
                new Condition().eq( "id", 2L )
        );
        // ## 3. 批量删除
        userActionHistoryService.deleteBatchIds(
                userActionHistoryService.selectList( null )
                                        .parallelStream()
                                        .map( UserActionHistory::getId )
                                        .collect( Collectors.toList() )
        );


    }
}








