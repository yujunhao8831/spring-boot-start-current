package com.goblin.common.drools;

import com.goblin.common.util.LogUtils;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pijingzhanji
 */
public class DroolsUtils {


    public static void main ( String[] args ) throws Exception {

        Map< String, Object > factMap = new HashMap<>();
        factMap.put( "edu", "大专" );
        factMap.put( "age", 18 );

        System.err.println( "factMap = " + factMap );

        execute( factMap , new RuleNameStartsWithAgendaFilter( "authenticate" ) );

        System.err.println( "factMap = " + factMap );
    }

    private static final Object         LOCK         = new Object();
    private static       KieContainer   kieContainer;
    private static       List< String > ruleContents = Arrays.asList(
            "package com.goblin\n" +
                    "import java.util.Map\n" +
                    "import java.lang.String\n" +
                    "\n" +
                    "rule \"authenticate_1\"\n" +
                    "salience 1\n" +
                    "when map:Map(true)\n" +
                    "then\n" +
                    "if(((String)(map.get(\"edu\"))).equals(\"大学\")){\n" +
                    "map.put(\"eduResult\",true);\n" +
                    "}\n" +
                    "else{\n" +
                    "map.put(\"eduResult\",false);\n" +
                    "}\n" +
                    "end" ,
            "package com.goblin\n" +
                    "import java.util.Map\n" +
                    "import java.lang.Integer\n" +
                    "\n" +
                    "rule \"authenticate_2\"\n" +
                    "salience 2\n" +
                    "when\n" +
                    "map:Map(true)\n" +
                    "then\n" +
                    "if(((Integer)(map.get(\"age\"))) >= 18 && ((Integer)(map.get(\"age\"))) <= 50){\n" +
                    "map.put(\"ageResult\",true);\n" +
                    "}\n" +
                    "else{\n" +
                    "map.put(\"ageResult\",false);\n" +
                    "}\n" +
                    "end\n"
    );


    /**
     * @param fact         事实
     * @param agendaFilter 具体选用拿个规则进行处理
     */
    public static void execute ( Object fact , AgendaFilter agendaFilter ) {
        KieContainer     kieContainer = getKieContainer();
        final KieSession kieSession   = kieContainer.newKieSession();
        // 事实
        kieSession.insert( fact );
        // 开始计算
        kieSession.fireAllRules( agendaFilter );
        // 销毁
        kieSession.destroy();
    }

    public static KieContainer getKieContainer () {
        if ( null == kieContainer ) {
            synchronized ( LOCK ) {
                if ( null == kieContainer ) {
                    return kieContainer = buildKieContainer( ruleContents );
                }
            }
        }
        return kieContainer;
    }

    private static KieContainer buildKieContainer ( List< String > ruleContents ) {
        // 获取的各种对象来完成规则构建、管理和执行等操作
        KieServices kieServices = KieServices.Factory.get();
        // 用于以编程方式定义组成KieModule的资源
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        for ( String ruleContent : ruleContents ) {
            kieFileSystem.write( "src/main/resources/" + System.currentTimeMillis() + ".drl" , ruleContent );
        }
        // KieBuilder是KieModule中包含的资源的构建者
        KieBuilder kieBuilder = kieServices.newKieBuilder( kieFileSystem ).buildAll();

        // 如果有错误
        if ( kieBuilder.getResults().hasMessages() ) {
            LogUtils.getLogger().warn( kieBuilder.getResults().getMessages() );
            if ( kieBuilder.getResults().hasMessages( Message.Level.ERROR ) ) {
                throw new RuntimeException( kieBuilder.getResults()
                                                      .getMessages()
                                                      .parallelStream()
                                                      .map( Message::getText ).collect( Collectors.joining( "," ) )
                );
            }
        }
        // 用来访问KBase和KSession等信息
        return kieServices.newKieContainer( kieServices.getRepository().getDefaultReleaseId() );
    }

}









