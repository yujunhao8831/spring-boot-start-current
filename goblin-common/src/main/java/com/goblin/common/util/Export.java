package com.goblin.common.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 导出. 在 Controller 中调用
 */
public final class Export {

    public static final  boolean excel07            = true;
    public static final  boolean excel03            = ! excel07;
    private static final String  DEFAULT_SHEET_NAME = "工作表1";

    /**
     * 导出文件! 在 Controller 中调用!
     *
     * @param type     类型: csv 或 xls xlsx 三种, 传 null 则默认是 csv
     * @param name     导出的文件名, 若导出成 excel, sheet 名也是这个
     * @param titleMap 标题(key 为英文, value 为标题内容)
     * @param dataList 导出的数据(数组中的每个 object 都是一行, object 中的属性名与标题中的 key 相对)
     */
    public static void export ( Type type , String name , LinkedHashMap< String, String > titleMap ,
                                List< ? > dataList , HttpServletResponse response ) throws IOException {
        if ( type == null ) type = Type.CSV;

        // 导出文件加上当前时间
        String fileName = encodeName( name ) + "." + type.getValue().toLowerCase();
        if ( type.isExcel() ) {
            LinkedHashMap linkedHashMap = new LinkedHashMap( Collections.singletonMap( name , dataList ) );
            exportXls( response , fileName , titleMap , linkedHashMap );
        } /*else if ("pdf".equalsIgnoreCase(type)) {
            // ...
        }*/ else {
            exportCsv( response , fileName , titleMap , dataList );
        }
    }

    public static String encodeName ( String name ) {
        String fileName  = name + "-" + ( DateUtils.currentTimeString() );
        String userAgent = RequestUtils.getUserAgentHeader();
        if ( StringUtils.isNotBlank( userAgent ) && userAgent.contains( "Mozilla" ) ) {
            // Chrome, Firefox, Safari etc...
            fileName = new String( fileName.getBytes() , StandardCharsets.ISO_8859_1 );
        } else {
            try {
                fileName = URLEncoder.encode( fileName , StandardCharsets.UTF_8.name() );
            } catch ( UnsupportedEncodingException e ) {
                // ignore
            }
        }
        return fileName;
    }

    private static void setResponse ( HttpServletResponse response , String type , String fileName ) {
        response.setContentType( "text/" + type );
        response.setContentType( "application/octet-stream; charset=utf-8" );
        response.setHeader( "Content-Disposition" , "attachment;filename=" + fileName );
    }

    public static void exportDoc ( HttpServletResponse response , String fileName , String html ) throws IOException {
        // 将 html 导出成 doc 文档
        setResponse( response , "doc" , fileName );

        POIFSFileSystem fs = new POIFSFileSystem();
        fs.createDocument( new ByteArrayInputStream( html.getBytes( StandardCharsets.UTF_8 ) ) , "WordDocument" );
        fs.writeFilesystem( response.getOutputStream() );
    }


    public static void exportXls ( HttpServletResponse response , String fileName ,
                                   LinkedHashMap< String, String > titleMap ,
                                   LinkedHashMap< String, List< ? > > dataMap ) throws IOException {
        // 导出 excel
        setResponse( response , "xls" , fileName );
        exportXls( response.getOutputStream() , Type.is07Suffix( fileName ) , titleMap , dataMap );
    }

    public static void exportXls ( OutputStream outputStream ,
                                   boolean isExcel07 ,
                                   String sheetName ,
                                   LinkedHashMap< String, String > titleMap ,
                                   List< ? > dataList ) throws IOException {
        exportXls( outputStream ,
                   isExcel07 ,
                   titleMap ,
                   new LinkedHashMap<>( Collections.singletonMap( sheetName , dataList ) ) );
    }

    public static void exportXls ( OutputStream outputStream ,
                                   LinkedHashMap< String, String > titleMap ,
                                   List< ? > dataList ) throws IOException {
        exportXls( outputStream ,
                   excel03 ,
                   titleMap ,
                   new LinkedHashMap<>( Collections.singletonMap( DEFAULT_SHEET_NAME , dataList ) )
        );
    }


    public static void exportXls ( OutputStream outputStream ,
                                   boolean isExcel07 ,
                                   LinkedHashMap< String, String > titleMap ,
                                   List< ? > dataList ) throws IOException {
        exportXls( outputStream ,
                   isExcel07 ,
                   titleMap ,
                   new LinkedHashMap<>( Collections.singletonMap( DEFAULT_SHEET_NAME , dataList ) ) );
    }

    /**
     * 导出Excel
     *
     * @param outputStream : 输出流
     * @param isExcel07    : 是否microsoft excel 2007(xlsx)
     * @param titleMap     : 属性名为 key, 对应的标题为 value
     * @param dataMap      : ( key : sheetname,value : sheet数据 ) , 如果你只要一个表,那么可以这样:new LinkedHashMap<>( Collections.singletonMap( sheetName , dataMap ) ) )
     * @throws IOException
     */
    public static void exportXls ( OutputStream outputStream , boolean isExcel07 ,
                                   LinkedHashMap< String, String > titleMap ,
                                   LinkedHashMap< String, List< ? > > dataMap ) throws IOException {
        exportXlsToWorkbook( isExcel07 , titleMap , dataMap ).write( outputStream );
    }

    public static Workbook exportXlsToWorkbook ( boolean isExcel07 ,
                                                 LinkedHashMap< String, String > titleMap ,
                                                 List< ? > dataList ) throws IOException {
        return ExportExcel.handle( isExcel07 ,
                                   titleMap ,
                                   new LinkedHashMap<>( Collections.singletonMap( DEFAULT_SHEET_NAME , dataList ) ) );
    }

    public static Workbook exportXlsToWorkbook ( boolean isExcel07 ,
                                                 LinkedHashMap< String, String > titleMap ,
                                                 LinkedHashMap< String, List< ? > > dataMap ) throws IOException {
        return ExportExcel.handle( isExcel07 , titleMap , dataMap );
    }


    public static void exportTxt ( HttpServletResponse response , String fileName , String content ) throws
                                                                                                     IOException {
        // 导出 txt
        setResponse( response , "plain" , fileName );
        response.getOutputStream().write( content.getBytes( StandardCharsets.UTF_8 ) );
    }

    public static void exportCsv ( HttpServletResponse response , String fileName ,
                                   LinkedHashMap< String, String > titleMap , List< ? > dataList ) throws IOException {
        String content = convertCsv( titleMap , dataList );

        // 导出 csv
        setResponse( response , "csv" , fileName );
        response.getOutputStream().write( content.getBytes( StandardCharsets.UTF_8 ) );

        POIFSFileSystem poifsFileSystem = new POIFSFileSystem();
        poifsFileSystem.createDocument( new ByteArrayInputStream( content.getBytes( "GBK" ) ) , "WordDocument" );
        poifsFileSystem.writeFilesystem( response.getOutputStream() );
    }

    private static String convertCsv ( LinkedHashMap< String, String > titleMap , List< ? > dataList ) {

        // 没有数据或没有标题, 返回一个内容为空的文件
        if ( MapUtils.isEmpty( titleMap ) || CollectionUtils.isEmpty( dataList ) ) return StringUtils.EMPTY;

        // 用英文逗号(,)隔开列, 用换行(\n)隔开行, 内容中包含了逗号的需要用双引号包裹, 若内容中包含了双引号则需要用两个双引号表示.
        StringBuilder sbd = new StringBuilder();
        int           i   = 0;
        for ( String title : titleMap.values() ) {
            sbd.append( handleCsvContent( title ) );
            i++;
            if ( i != titleMap.size() ) sbd.append( "," );
        }
        for ( Object data : dataList ) {
            sbd.append( "\n" );
            i = 0;
            for ( String title : titleMap.keySet() ) {
                sbd.append( handleCsvContent(
					ReflectionProUtils.invokeFieldGettersMethodToCustomizeString( data , title ) )
                );
                i++;
                if ( i != titleMap.size() ) sbd.append( "," );
            }
        }
        return sbd.toString();
    }

    private static String handleCsvContent ( String content ) {
        return "\"" + content.replace( "\"" , "\"\"" ) + "\"";
    }

    public static String addXlsSuffix ( String fileName ) {
        return StringProUtils.getString( fileName ) + ".xls";
    }

    public static String addXlsxSuffix ( String fileName ) {
        return StringProUtils.getString( fileName ) + ".xlsx";
    }

    public enum Type {
        MS03( "xls" ), MS07( "xlsx" ), CSV( "csv" );

        private String value;

        Type ( String value ) {
            this.value = value;
        }

        public static boolean is07Suffix ( String fileName ) {
            return fileName.toLowerCase().endsWith( Type.MS07.getValue().toLowerCase() );
        }

        public static boolean is03Suffix ( String fileName ) {
            return fileName.toLowerCase().endsWith( Type.MS03.getValue().toLowerCase() );
        }

        public String getValue () {
            return value;
        }

        public boolean isExcel () {
            return Arrays.asList( MS03 , MS07 ).contains( this );
        }
    }

    private static final class ExportExcel {
        /** excel 2003 的最大列数是 256 列, 2007 及以上版本是 16384 列 */
        private static final int CELL_TOTAL = 256;
        /** excel 2003 的最大行数是 65536 行, 等同于 (2 << 15) - 1, 2007 开始的版本是 1048576 行 */
        private static final int ROW_TOTAL  = 65536;

        /** 标题行的字体大小 */
        private static final short HEAD_FONT_SIZE = 11;
        /** 其他内容的字体大小 */
        private static final short FONT_SIZE      = 10;
        /** 行高. 要比上面的字体大一点! */
        private static final short ROW_HEIGHT     = 15;

        private static boolean must07 ( int cellSize ) {
            return cellSize > CELL_TOTAL;
        }

        /**
         * 返回一个 excel 工作簿
         *
         * @param excel07  是否返回 microsoft excel 2007 的版本
         * @param titleMap 属性名为 key, 对应的标题为 value, 为了处理显示时的顺序, 因此使用 linkedHashMap
         * @param dataMap  以「sheet 名」为 key, 对应的数据为 value(每一行的数据为一个 Object)
         */
        private static Workbook handle ( boolean excel07 , LinkedHashMap< String, String > titleMap ,
                                         LinkedHashMap< String, List< ? > > dataMap ) {
            // 如果要导出的不是 07 的版本, 但是导出的列却大于 256 列, 则只能导出 07 版本
            if ( ! excel07 ) {
                excel07 = must07( titleMap.size() );
            }

            // 声明一个工作薄. HSSFWorkbook 是 Office 2003 的版本, XSSFWorkbook 是 2007
            Workbook workbook = excel07 ? new XSSFWorkbook() : new HSSFWorkbook();
            // 没有数据, 或者没有标题, 都直接返回
            if ( MapUtils.isEmpty( dataMap ) || MapUtils.isEmpty( titleMap ) ) return workbook;

            // 头样式
            CellStyle headStyle = createHeadStyle( workbook );
            // 内容样式
            CellStyle contentStyle = createContentStyle( workbook );

            // sheet
            Sheet sheet;
            // 行
            Row row;
            // 列
            Cell cell;
            // 表格数        行索引     列索引      数据起始索引  数据结束索引
            int sheetCount, rowIndex, cellIndex, fromIndex, toIndex;
            //  大小   数据
            int       size;
            List< ? > excelList;

            for ( Map.Entry< String, List< ? > > entry : dataMap.entrySet() ) {
                // 当前 sheet 的数据
                excelList = entry.getValue();
                if ( CollectionUtils.isNotEmpty( excelList ) ) {
                    // 一个 sheet 数据过多 excel 处理会出错, 分多个 sheet
                    size = excelList.size();
                    sheetCount = ( ( size % ROW_TOTAL == 0 ) ? ( size / ROW_TOTAL ) : ( size / ROW_TOTAL + 1 ) );
                    for ( int i = 0 ; i < sheetCount ; i++ ) {
                        // 构建 sheet, 带名字
                        sheet = workbook.createSheet( entry.getKey() + ( sheetCount > 1
                                                                         ? ( "-" + ( i + 1 ) )
                                                                         : StringUtils.EMPTY ) );

                        // 每个 sheet 的标题行
                        rowIndex = 0;
                        cellIndex = 0;
                        row = sheet.createRow( rowIndex );
                        row.setHeightInPoints( ROW_HEIGHT );
                        // 每个 sheet 的标题行
                        for ( String header : titleMap.values() ) {
                            // 宽度自适应
                            sheet.autoSizeColumn( cellIndex );
                            cell = row.createCell( cellIndex );
                            cell.setCellStyle( headStyle );
                            cell.setCellValue( header );
                            cellIndex++;
                        }
                        // 冻结第一行
                        sheet.createFreezePane( 0 , 1 , 0 , 1 );

                        // 每个 sheet 除标题行以外的数据
                        fromIndex = ROW_TOTAL * i;
                        toIndex = ( i + 1 == sheetCount ) ? size : ROW_TOTAL;
                        for ( Object data : excelList.subList( fromIndex , toIndex ) ) {
                            if ( data != null ) {
                                rowIndex++;
                                // 每行
                                row = sheet.createRow( rowIndex );
                                row.setHeightInPoints( ROW_HEIGHT );
                                cellIndex = 0;
                                for ( String value : titleMap.keySet() ) {
                                    String cellValue;
                                    if ( data instanceof Map ) {
                                        final Object obj = ( ( Map ) data ).get( value );
                                        cellValue = Objects.isNull( obj ) ? StringUtils.EMPTY : obj.toString();
                                    } else {
                                        cellValue =
                                            ReflectionProUtils.invokeFieldGettersMethodToCustomizeString( data ,
																										  value );
                                    }
                                    // 每列
                                    cell = row.createCell( cellIndex );
                                    cell.setCellStyle( contentStyle );
                                    cell.setCellValue( cellValue );
                                    cellIndex++;
                                }
                            }
                        }
                    }
                }
            }
            return workbook;
        }

        /** 头样式 */
        private static CellStyle createHeadStyle ( Workbook workbook ) {
            CellStyle style = workbook.createCellStyle();

            style.setAlignment( HorizontalAlignment.LEFT ); // 水平居左
            style.setVerticalAlignment( VerticalAlignment.CENTER ); // 垂直居中
            // style.setWrapText(true); // 自动换行

            Font font = workbook.createFont();
            font.setBold( true ); // 粗体
            font.setFontHeightInPoints( HEAD_FONT_SIZE );
            style.setFont( font );
            return style;
        }

        /** 内容样式 */
        private static CellStyle createContentStyle ( Workbook workbook ) {
            CellStyle style = workbook.createCellStyle();

            style.setAlignment( HorizontalAlignment.LEFT );
            style.setVerticalAlignment( VerticalAlignment.CENTER );

            Font font = workbook.createFont();
            font.setFontHeightInPoints( FONT_SIZE );
            style.setFont( font );
            return style;
        }
    }
}
