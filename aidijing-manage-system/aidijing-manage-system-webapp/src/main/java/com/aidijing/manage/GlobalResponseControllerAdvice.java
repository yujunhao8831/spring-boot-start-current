package com.aidijing.manage;

import com.aidijing.common.ResponseEntity;
import com.aidijing.common.annotation.NeedExport;
import com.aidijing.common.util.Export;
import com.aidijing.common.util.JsonUtils;
import com.aidijing.common.util.LogUtils;
import com.aidijing.common.util.ReflectionPlusUtils;
import com.aidijing.manage.bean.domain.RolePermissionResource;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * api {@link ResponseEntity} 返回类型处理,针对 {@link RolePermissionResource#resourceApiUriShowFields} 字段
 *
 * @author : 披荆斩棘
 * @date : 2017/6/29
 */
@RestControllerAdvice
public class GlobalResponseControllerAdvice implements ResponseBodyAdvice< ResponseEntity > {


	@Override
	public boolean supports ( MethodParameter returnType , Class converterType ) {
		return ResponseEntity.class.isAssignableFrom( returnType.getParameterType() );
	}

	@Override
	public ResponseEntity beforeBodyWrite ( ResponseEntity body ,
											MethodParameter returnType ,
											MediaType selectedContentType ,
											Class selectedConverterType ,
											ServerHttpRequest request ,
											ServerHttpResponse response ) {

		// 获取导出字段标题
		LinkedHashMap< String, String > exportTitleMap = body.getExportTitleMap();

		// | 1. 字段过滤处理 |
		// - 如果自行设置了,那么就以自行设置的为主
		if ( body.isFieldsFilter() ) {
			body = body.filterFieldsFlush();
		}
		// - 用户权限或者用户自定义处理
		final RolePermissionResource currentRequestRolePermissionResource = ContextUtils.getCurrentRequestRolePermissionResource();
		if ( Objects.nonNull( currentRequestRolePermissionResource ) ) {
			if ( ! ResponseEntity.WILDCARD_ALL.equals( currentRequestRolePermissionResource.getResourceApiUriShowFields() ) ) {
				// TODO: 2017/7/22 权限或者自定义就没有分页的了?
				body = body.setFilterFieldsAndFlush( currentRequestRolePermissionResource.getResourceApiUriShowFields() );
			}
		}

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// | 2. 导出处理 | 这里写在一个类中,显得臃肿,后续调整
		HttpServletRequest  servletRequest  = ( ( ServletServerHttpRequest ) request ).getServletRequest();
		HttpServletResponse servletResponse = ( ( ServletServerHttpResponse ) response ).getServletResponse();

		if ( this.isExport( servletRequest , returnType ) ) {
			return this.exportHandle( body ,
									  returnType ,
									  selectedContentType ,
									  selectedConverterType ,
									  exportTitleMap ,
									  servletRequest ,
									  servletResponse );
		}
		return body;
	}


	/**
	 * 导出处理
	 *
	 * @param body
	 * @param returnType
	 * @param selectedContentType
	 * @param selectedConverterType
	 * @param exportTitleMap
	 * @param request
	 * @param response
	 * @return
	 */
	private ResponseEntity exportHandle ( ResponseEntity body ,
										  MethodParameter returnType ,
										  MediaType selectedContentType ,
										  Class selectedConverterType ,
										  LinkedHashMap< String, String > exportTitleMap ,
										  HttpServletRequest request ,
										  HttpServletResponse response ) {
		// 服务器导出配置
		final NeedExport needExport = returnType.getMethod().getAnnotation( NeedExport.class );
		// 客户端自定义的导出配置
		ExportParamsMessage exportParamsMessage = this.extractExportParamsMessage( request , needExport );
		// 得到导出数据
		List              dataList              = this.getExportList( body );
		final Export.Type exportType            = this.getExportType( needExport , exportParamsMessage );
		final String      exportFileDefaultName = this.getExportFileDefaultName( needExport , exportParamsMessage );

		final LinkedHashMap< String, String > titleMap = exportTitleMap == null
														 ? ReflectionPlusUtils.exportFiledComment( needExport.exportClass() )
														 : exportTitleMap;


		LogUtils.getLogger().debug( "exportType : {}" , exportType );
		LogUtils.getLogger().debug( "exportFileDefaultName : {}" , exportFileDefaultName );
		LogUtils.getLogger().debug( "titleMap : {}" , titleMap );
		try {
			Export.export( exportType , exportFileDefaultName , titleMap , dataList , response );
			return body;
		} catch ( IOException e ) {
			LogUtils.getLogger().error( e );
			return ResponseEntity.badRequest( "导出异常:" , e.getMessage() );
		}
	}

	/**
	 * 提取客户端导出参数
	 *
	 * @param request
	 * @param needExport
	 * @return
	 */
	private ExportParamsMessage extractExportParamsMessage ( HttpServletRequest request ,
															 NeedExport needExport ) {
		final String headerExportParamsMessage = this.extractExportMessageFromRequest( request , "export" );
		LogUtils.getLogger().debug( "needExport : {}" , needExport );
		ExportParamsMessage exportParamsMessage =
			JsonUtils.jsonToType( headerExportParamsMessage , ExportParamsMessage.class );
		// 解析失败,给个空,这样才不会影响正常流程
		if ( Objects.isNull( exportParamsMessage ) ) {
			exportParamsMessage = ExportParamsMessage.EMPTY;
		}
		return exportParamsMessage;
	}

	/**
	 * 得到导出数据
	 *
	 * @param body
	 * @return
	 */
	private List getExportList ( ResponseEntity body ) {
		final Object responseContent = body.getResponseContent();
		List         dataList;
		if ( responseContent instanceof List ) {
			dataList = ( List ) responseContent;
		} else if ( responseContent instanceof PageInfo ) {
			dataList = ( ( PageInfo ) responseContent ).getList();
		} else if ( responseContent instanceof Map ) {
			dataList = ( List ) ( ( Map ) responseContent ).get( "list" );
		} else {
			dataList = Collections.singletonList( responseContent );
		}
		return dataList;
	}

	/**
	 * 得到导出文件名
	 *
	 * @param needExport
	 * @param exportParamsMessage
	 * @return
	 */
	private String getExportFileDefaultName ( NeedExport needExport ,
											  ExportParamsMessage exportParamsMessage ) {
		if ( StringUtils.isNotEmpty( exportParamsMessage.getExportFileName() ) ) {
			return exportParamsMessage.getExportFileName();
		}
		return needExport.exportFileDefaultName();
	}

	/**
	 * 得到导出类型
	 *
	 * @param needExport
	 * @param exportParamsMessage
	 * @return
	 */
	private Export.Type getExportType ( NeedExport needExport ,
										ExportParamsMessage exportParamsMessage ) {
		if ( Objects.nonNull( exportParamsMessage.getExportType() ) ) {
			return exportParamsMessage.getExportType();
		}
		return needExport.exportDefaultType();
	}

	/**
	 * 是否是导出
	 *
	 * @param request
	 * @param returnType
	 * @return
	 */
	private boolean isExport ( HttpServletRequest request , MethodParameter returnType ) {
		final Method     method              = returnType.getMethod();
		final NeedExport needExport          = method.getAnnotation( NeedExport.class );
		final String     exportParamsMessage = this.extractExportMessageFromRequest( request , "export" );
		final boolean    isExportHeader      = Objects.nonNull( exportParamsMessage );
		final boolean    isNeedExport        = Objects.nonNull( needExport );
		return isExportHeader && isNeedExport;
	}

	/**
	 * 请求中获取导出信息
	 *
	 * @param request
	 * @return
	 */
	private String extractExportMessageFromRequest ( HttpServletRequest request , String headerName ) {
		String header = request.getHeader( headerName );
		if ( StringUtils.isEmpty( header ) ) {
			header = request.getParameter( headerName );
		}
		LogUtils.getLogger().debug( "{} : {}" , headerName , header );
		return header;
	}


	@Data
	@Accessors( chain = true )
	private static class ExportParamsMessage implements Serializable {
		public static final  ExportParamsMessage EMPTY            = new ExportParamsMessage();
		/**
		 *
		 */
		private static final long                serialVersionUID = 6012182515631669157L;
		// 无意义字段,只是一个标识
		private String      export;
		private Export.Type exportType;
		private String      exportFileName;
	}

}

