package com.aidijing.generator.mybatis.plus.config;//package com.aidijing.generator.config;///**
//// * Copyright (c) 2011-2020, hubin (jobob@qq.com).
//// * <p>
//// * Licensed under the Apache License, Version 2.0 (the "License"); you may not
//// * use this file except in compliance with the License. You may obtain a copy of
//// * the License at
//// * <p>
//// * http://www.apache.org/licenses/LICENSE-2.0
//// * <p>
//// * Unless required by applicable law or agreed to in writing, software
//// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
//// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
//// * License for the specific language governing permissions and limitations under
//// * the License.
//// */
////package com.aidijing.generator.config;
////
////import com.baomidou.mybatisplus.generator.AbstractGenerator;
////import com.baomidou.mybatisplus.generator.config.ConstVal;
////import com.baomidou.mybatisplus.generator.config.FileOutConfig;
////import com.baomidou.mybatisplus.generator.config.TemplateConfig;
////import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
////import com.baomidou.mybatisplus.generator.config.po.TableField;
////import com.baomidou.mybatisplus.generator.config.po.TableInfo;
////import com.baomidou.mybatisplus.toolkit.CollectionUtils;
////import com.baomidou.mybatisplus.toolkit.StringUtils;
////import org.apache.ibatis.logging.Log;
////import org.apache.ibatis.logging.LogFactory;
////import org.apache.velocity.Template;
////import org.apache.velocity.VelocityContext;
////import org.apache.velocity.app.Velocity;
////import org.apache.velocity.app.VelocityEngine;
////
////import java.io.*;
////import java.text.SimpleDateFormat;
////import java.util.*;
////
/////**
//// * 生成文件
//// *
//// * @author YangHu , tangguo
//// * @since 2016-08-30
//// */
////public class MyAutoGenerator extends AbstractGenerator {
////
////    private static final Log     logger               = LogFactory.getLog( com.baomidou.mybatisplus.generator.AutoGenerator.class );
////    private static       boolean separatorBeforeDigit = false;
////    private static       boolean separatorAfterDigit  = true;
////    /**
////     * velocity引擎
////     */
////    private VelocityEngine engine;
////
////    /**
////     * 第一个首字母小写
////     * 
////     * @param inputString
////     * @return
////     */
////    public static String firstCharToLower ( String inputString ) {
////        return prefixToLower( inputString, 1 );
////    }
////
////    /**
////     * 前n个首字母小写
////     *
////     * @param inputString
////     * @param index
////     * @return
////     */
////    public static String prefixToLower ( String inputString, int index ) {
////        String beforeChar = inputString.substring( 0, index ).toLowerCase();
////        String afterChar  = inputString.substring( index, inputString.length() );
////        return beforeChar + afterChar;
////    }
////
////    /**
////     * 删除前缀后,首字母小写
////     *
////     * @param inputString
////     * @param index
////     * @return
////     */
////    public static String removePrefixAfterPrefixToLower ( String inputString, int index ) {
////        return prefixToLower( inputString.substring( index, inputString.length() ), 1 );
////    }
////
////    /**
////     * 驼峰转连字符
////     * <pre>
////     *     StringUtils.camelToHyphen( "managerAdminUserService" ) = manager-admin-user-service
////     * </pre>
////     *
////     * @param input
////     * @return 以'-'分隔
////     * @see <a href="https://github.com/krasa/StringManipulation">document</a>
////     */
////    public static String camelToHyphen ( String input ) {
////        return wordsToHyphenCase( wordsAndHyphenAndCamelToConstantCase( input ) );
////    }
////
////    private static String wordsAndHyphenAndCamelToConstantCase ( String input ) {
////        boolean _betweenUpperCases = false;
////        boolean containsLowerCase  = containsLowerCase( input );
////
////        StringBuilder buf          = new StringBuilder();
////        char          previousChar = ' ';
////        char[]        chars        = input.toCharArray();
////        for ( int i = 0 ; i < chars.length ; i++ ) {
////            char c = chars[i];
////            boolean isUpperCaseAndPreviousIsUpperCase = ( Character.isUpperCase( previousChar ) ) && ( Character.isUpperCase(
////                    c ) );
////            boolean isUpperCaseAndPreviousIsLowerCase = ( Character.isLowerCase( previousChar ) ) && ( Character.isUpperCase(
////                    c ) );
////
////            boolean previousIsWhitespace   = Character.isWhitespace( previousChar );
////            boolean lastOneIsNotUnderscore = ( buf.length() > 0 ) && ( buf.charAt( buf.length() - 1 ) != '_' );
////            boolean isNotUnderscore        = c != '_';
////            if ( ( lastOneIsNotUnderscore ) && ( ( isUpperCaseAndPreviousIsLowerCase ) || ( previousIsWhitespace ) || ( ( _betweenUpperCases ) && ( containsLowerCase ) && ( isUpperCaseAndPreviousIsUpperCase ) ) ) ) {
////                buf.append( "_" );
////            } else if ( ( ( separatorAfterDigit ) && ( Character.isDigit( previousChar ) ) && ( Character.isLetter( c ) ) ) || ( ( separatorBeforeDigit ) && ( Character
////                    .isDigit( c ) ) && ( Character.isLetter( previousChar ) ) ) ) {
////                buf.append( '_' );
////            }
////            if ( ( shouldReplace( c ) ) && ( lastOneIsNotUnderscore ) ) {
////                buf.append( '_' );
////            } else if ( ( ! Character.isWhitespace( c ) ) && ( ( isNotUnderscore ) || ( lastOneIsNotUnderscore ) ) ) {
////                buf.append( Character.toUpperCase( c ) );
////            }
////            previousChar = c;
////        }
////        if ( Character.isWhitespace( previousChar ) ) {
////            buf.append( "_" );
////        }
////        return buf.toString();
////    }
////
////    public static boolean containsLowerCase ( String s ) {
////        for ( char c : s.toCharArray() ) {
////            if ( Character.isLowerCase( c ) ) {
////                return true;
////            }
////        }
////        return false;
////    }
////
////    private static boolean shouldReplace ( char c ) {
////        return ( c == '.' ) || ( c == '_' ) || ( c == '-' );
////    }
////
////    private static String wordsToHyphenCase ( String s ) {
////        StringBuilder buf      = new StringBuilder();
////        char          lastChar = 'a';
////        for ( char c : s.toCharArray() ) {
////            if ( ( Character.isWhitespace( lastChar ) ) && ( ! Character.isWhitespace( c ) ) && ( '-' != c ) && ( buf.length() > 0 ) && ( buf
////                    .charAt( buf.length() - 1 ) != '-' ) ) {
////                buf.append( "-" );
////            }
////            if ( '_' == c ) {
////                buf.append( '-' );
////            } else if ( '.' == c ) {
////                buf.append( '-' );
////            } else if ( ! Character.isWhitespace( c ) ) {
////                buf.append( Character.toLowerCase( c ) );
////            }
////            lastChar = c;
////        }
////        if ( Character.isWhitespace( lastChar ) ) {
////            buf.append( "-" );
////        }
////        return buf.toString();
////    }
////
////    /**
////     * 生成代码
////     */
////    public void execute () {
////        logger.debug( "==========================准备生成文件...==========================" );
////        // 初始化配置
////        initConfig();
////        // 创建输出文件路径
////        mkdirs( config.getPathInfo() );
////        // 获取上下文
////        Map< String, VelocityContext > ctxData = analyzeData( config );
////        // 循环生成文件
////        for ( Map.Entry< String, VelocityContext > ctx : ctxData.entrySet() ) {
////            batchOutput( ctx.getKey(), ctx.getValue() );
////        }
////        // 打开输出目录
////        if ( config.getGlobalConfig().isOpen() ) {
////            try {
////                String osName = System.getProperty( "os.name" );
////                if ( osName != null ) {
////                    if ( osName.contains( "Mac" ) ) {
////                        Runtime.getRuntime().exec( "open " + config.getGlobalConfig().getOutputDir() );
////                    } else if ( osName.contains( "Windows" ) ) {
////                        Runtime.getRuntime().exec( "cmd /c start " + config.getGlobalConfig().getOutputDir() );
////                    } else {
////                        logger.debug( "文件输出目录:" + config.getGlobalConfig().getOutputDir() );
////                    }
////                }
////            } catch ( IOException e ) {
////                e.printStackTrace();
////            }
////        }
////        logger.debug( "==========================文件生成完成！！！==========================" );
////    }
////
////    /**
////     * <p>
////     * 开放表信息、预留子类重写
////     * </p>
////     *
////     * @param config 配置信息
////     * @return
////     */
////    protected List< TableInfo > getAllTableInfoList ( ConfigBuilder config ) {
////        return config.getTableInfoList();
////    }
////
////    /**
////     * 分析数据
////     *
////     * @param config 总配置信息
////     * @return 解析数据结果集
////     */
////    private Map< String, VelocityContext > analyzeData ( ConfigBuilder config ) {
////
////        List< TableInfo >              tableList             = this.getAllTableInfoList( config );
////        Map< String, String >          packageInfo           = config.getPackageInfo();
////        Map< String, VelocityContext > ctxData               = new HashMap<>();
////        String                         superEntityClass      = getSuperClassName( config.getSuperEntityClass() );
////        String                         superMapperClass      = getSuperClassName( config.getSuperMapperClass() );
////        String                         superServiceClass     = getSuperClassName( config.getSuperServiceClass() );
////        String                         superServiceImplClass = getSuperClassName( config.getSuperServiceImplClass() );
////        String                         superControllerClass  = getSuperClassName( config.getSuperControllerClass() );
////        String                         date                  = new SimpleDateFormat( "yyyy-MM-dd" ).format( new Date() );
////        VelocityContext                ctx;
////        for ( TableInfo tableInfo : tableList ) {
////            ctx = new VelocityContext();
////            if ( null != injectionConfig ) {
////                /**
////                 * 注入自定义配置
////                 */
////                injectionConfig.initMap();
////                ctx.put( "cfg", injectionConfig.getMap() );
////            }
////            /* ---------- 添加导入包 ---------- */
////            if ( config.getGlobalConfig().isActiveRecord() ) {
////                // 开启 ActiveRecord 模式
////                tableInfo.setImportPackages( "com.baomidou.mybatisplus.activerecord.Model" );
////            }
////            if ( tableInfo.isConvert() ) {
////                // 表注解
////                tableInfo.setImportPackages( "com.baomidou.mybatisplus.annotations.TableName" );
////            }
////            if ( StringUtils.isNotEmpty( config.getSuperEntityClass() ) ) {
////                // 父实体
////                tableInfo.setImportPackages( config.getSuperEntityClass() );
////            } else {
////                tableInfo.setImportPackages( "java.io.Serializable" );
////            }
////
////            String primaryKeyTypeString = null;
////            
////            // Boolean类型is前缀处理
////            for ( TableField field : tableInfo.getFields() ) {
////                if ( field.getPropertyType().equalsIgnoreCase( "boolean" ) ) {
////                    if ( field.getPropertyName().indexOf( "is" ) != - 1 ) {
////                        field.setPropertyName(
////                                config.getStrategyConfig(),
////                                removePrefixAfterPrefixToLower( field.getPropertyName(), 2 )
////                        );
////                    }
////                }
////
////                if ( field.isKeyFlag() && primaryKeyTypeString == null ) {
////                    primaryKeyTypeString = field.getPropertyType();
////                }
////            }
////
////
////            // RequestMapping 连字符风格 user-info
////            ctx.put( "controllerMappingHyphenStyle", true );
////            ctx.put( "controllerMappingHyphen", camelToHyphen( tableInfo.getEntityPath() ) );
////            ctx.put( "serviceVariableName", firstCharToLower( tableInfo.getServiceName() ) );
////            ctx.put( "entityVariableName", firstCharToLower( tableInfo.getEntityName() ) );
////            ctx.put( "primaryKeyTypeString", primaryKeyTypeString );
////          
////            ctx.put( "restControllerStyle", true );
////            ctx.put( "package", packageInfo );
////            ctx.put( "author", config.getGlobalConfig().getAuthor() );
////            ctx.put( "activeRecord", config.getGlobalConfig().isActiveRecord() );
////            ctx.put( "date", date );
////            ctx.put( "table", tableInfo );
////            ctx.put( "enableCache", config.getGlobalConfig().isEnableCache() );
////            ctx.put( "baseResultMap", config.getGlobalConfig().isBaseResultMap() );
////            ctx.put( "baseColumnList", config.getGlobalConfig().isBaseColumnList() );
////            ctx.put( "entity", tableInfo.getEntityName() );
////            ctx.put( "entityColumnConstant", config.getStrategyConfig().isEntityColumnConstant() );
////            ctx.put( "entityBuilderModel", config.getStrategyConfig().isEntityBuilderModel() );
////            ctx.put( "entityLombokModel", true );
////            ctx.put( "entityBooleanColumnRemoveIsPrefix", true );
////            ctx.put( "superEntityClass", superEntityClass );
////            ctx.put( "superMapperClassPackage", config.getSuperMapperClass() );
////            ctx.put( "superMapperClass", superMapperClass );
////            ctx.put( "superServiceClassPackage", config.getSuperServiceClass() );
////            ctx.put( "superServiceClass", superServiceClass );
////            ctx.put( "superServiceImplClassPackage", config.getSuperServiceImplClass() );
////            ctx.put( "superServiceImplClass", superServiceImplClass );
////            ctx.put( "superControllerClassPackage", config.getSuperControllerClass() );
////            ctx.put( "superControllerClass", superControllerClass );
////            ctxData.put( tableInfo.getEntityName(), ctx );
////        }
////        return ctxData;
////    }
////
////    /**
////     * 获取类名
////     *
////     * @param classPath
////     * @return
////     */
////    private String getSuperClassName ( String classPath ) {
////        if ( StringUtils.isEmpty( classPath ) )
////            return null;
////        return classPath.substring( classPath.lastIndexOf( "." ) + 1 );
////    }
////
////    /**
////     * 处理输出目录
////     *
////     * @param pathInfo 路径信息
////     */
////    private void mkdirs ( Map< String, String > pathInfo ) {
////        for ( Map.Entry< String, String > entry : pathInfo.entrySet() ) {
////            File dir = new File( entry.getValue() );
////            if ( ! dir.exists() ) {
////                boolean result = dir.mkdirs();
////                if ( result ) {
////                    logger.debug( "创建目录： [" + entry.getValue() + "]" );
////                }
////            }
////        }
////    }
////
////    /**
////     * 合成上下文与模板
////     *
////     * @param context vm上下文
////     */
////    private void batchOutput ( String entityName, VelocityContext context ) {
////        try {
////            TableInfo             tableInfo = ( TableInfo ) context.get( "table" );
////            Map< String, String > pathInfo  = config.getPathInfo();
////            String entityFile = String.format(
////                    ( pathInfo.get( ConstVal.ENTITY_PATH ) + ConstVal.ENTITY_NAME ),
////                    entityName
////            );
////            String mapperFile = String.format( ( pathInfo.get( ConstVal.MAPPER_PATH ) + File.separator + tableInfo
////                    .getMapperName() + ConstVal.JAVA_SUFFIX ), entityName );
////            String xmlFile = String.format( ( pathInfo.get( ConstVal.XML_PATH ) + File.separator + tableInfo
////                    .getXmlName() + ConstVal.XML_SUFFIX ), entityName );
////            String serviceFile = String.format( ( pathInfo.get( ConstVal.SERIVCE_PATH ) + File.separator + tableInfo
////                    .getServiceName() + ConstVal.JAVA_SUFFIX ), entityName );
////            String implFile = String.format( ( pathInfo.get( ConstVal.SERVICEIMPL_PATH ) + File.separator + tableInfo
////                    .getServiceImplName() + ConstVal.JAVA_SUFFIX ), entityName );
////            String controllerFile = String.format( ( pathInfo.get( ConstVal.CONTROLLER_PATH ) + File.separator + tableInfo
////                    .getControllerName() + ConstVal.JAVA_SUFFIX ), entityName );
////
////            TemplateConfig template = config.getTemplate();
////
////            // 根据override标识来判断是否需要创建文件
////            if ( isCreate( entityFile ) ) {
////                vmToFile( context, template.getEntity(), entityFile );
////            }
////            if ( isCreate( mapperFile ) ) {
////                vmToFile( context, template.getMapper(), mapperFile );
////            }
////            if ( isCreate( xmlFile ) ) {
////                vmToFile( context, template.getXml(), xmlFile );
////            }
////            if ( isCreate( serviceFile ) ) {
////                vmToFile( context, template.getService(), serviceFile );
////            }
////            if ( isCreate( implFile ) ) {
////                vmToFile( context, template.getServiceImpl(), implFile );
////            }
////            if ( isCreate( controllerFile ) ) {
////                vmToFile( context, template.getController(), controllerFile );
////            }
////            if ( injectionConfig != null ) {
////                /**
////                 * 输出自定义文件内容
////                 */
////                List< FileOutConfig > focList = injectionConfig.getFileOutConfigList();
////                if ( CollectionUtils.isNotEmpty( focList ) ) {
////                    for ( FileOutConfig foc : focList ) {
////                        vmToFile( context, foc.getTemplatePath(), foc.outputFile( tableInfo ) );
////                    }
////                }
////            }
////
////        } catch ( IOException e ) {
////            logger.error( "无法创建文件，请检查配置信息！", e );
////        }
////    }
////
////    /**
////     * 将模板转化成为文件
////     *
////     * @param context      内容对象
////     * @param templatePath 模板文件
////     * @param outputFile   文件生成的目录
////     */
////    private void vmToFile ( VelocityContext context, String templatePath, String outputFile ) throws IOException {
////        if ( StringUtils.isEmpty( templatePath ) ) {
////            return;
////        }
////        VelocityEngine velocity = getVelocityEngine();
////        Template       template = velocity.getTemplate( templatePath, ConstVal.UTF8 );
////        File           file     = new File( outputFile );
////        if ( ! file.getParentFile().exists() ) {
////            // 如果文件所在的目录不存在，则创建目录
////            if ( ! file.getParentFile().mkdirs() ) {
////                logger.debug( "创建文件所在的目录失败!" );
////                return;
////            }
////        }
////        FileOutputStream fos    = new FileOutputStream( outputFile );
////        BufferedWriter   writer = new BufferedWriter( new OutputStreamWriter( fos, ConstVal.UTF8 ) );
////        template.merge( context, writer );
////        writer.close();
////        logger.debug( "模板:" + templatePath + ";  文件:" + outputFile );
////    }
////
////    /**
////     * 设置模版引擎，主要指向获取模版路径
////     */
////    private VelocityEngine getVelocityEngine () {
////        if ( engine == null ) {
////            Properties p = new Properties();
////            p.setProperty( ConstVal.VM_LOADPATH_KEY, ConstVal.VM_LOADPATH_VALUE );
////            p.setProperty( Velocity.FILE_RESOURCE_LOADER_PATH, "" );
////            p.setProperty( Velocity.ENCODING_DEFAULT, ConstVal.UTF8 );
////            p.setProperty( Velocity.INPUT_ENCODING, ConstVal.UTF8 );
////            p.setProperty( Velocity.OUTPUT_ENCODING, ConstVal.UTF8 );
////            p.setProperty( "file.resource.loader.unicode", "true" );
////            engine = new VelocityEngine( p );
////        }
////        return engine;
////    }
////
////    /**
////     * 检测文件是否存在
////     *
////     * @return 是否
////     */
////    private boolean isCreate ( String filePath ) {
////        File file = new File( filePath );
////        return ! file.exists() || config.getGlobalConfig().isFileOverride();
////    }
////}
