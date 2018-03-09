package com.goblin.common;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.goblin.common.util.JsonUtils.jsonToType;
import static com.goblin.common.util.JsonUtils.toFilterJson;

/**
 * {@link ResponseEntity} 封装
 *
 * @author : 披荆斩棘
 * @date : 16/6/16
 */
@ToString
@Getter
@Setter
@Accessors( chain = true )
public class ResponseEntityPro {
	/** 通配符 **/
	public static final String        WILDCARD_ALL       = "*";
	public static final TypeReference MAP_TYPE_REFERENCE = new TypeReference< Map< String, Object > >() {
	};
	private volatile Map< String, Object > body;



	public static ResponseEntity< String > forbidden () {
		return status( HttpStatus.FORBIDDEN , HttpStatus.FORBIDDEN.getReasonPhrase() );
	}

	public static < T > ResponseEntity< T > forbidden ( final T body ) {
		return status( HttpStatus.FORBIDDEN , body );
	}

	public static < T > ResponseEntity< T > forbidden ( final T body , final String filterFields ) {
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return notFound( body );
		}
		return status( HttpStatus.FORBIDDEN , body , filterFields );
	}

	public static ResponseEntity< String > unauthorized () {
		return status( HttpStatus.UNAUTHORIZED , HttpStatus.UNAUTHORIZED.getReasonPhrase() );
	}

	public static < T > ResponseEntity< T > unauthorized ( final T body ) {
		return status( HttpStatus.UNAUTHORIZED , body );
	}

	public static < T > ResponseEntity< T > unauthorized ( final T body , final String filterFields ) {
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return notFound( body );
		}
		return status( HttpStatus.UNAUTHORIZED , body , filterFields );
	}


	public static ResponseEntity< String > notFound () {
		return status( HttpStatus.NOT_FOUND , HttpStatus.NOT_FOUND.getReasonPhrase() );
	}

	public static < T > ResponseEntity< T > notFound ( final T body ) {
		return status( HttpStatus.NOT_FOUND , body );
	}

	public static < T > ResponseEntity< T > notFound ( final T body , final String filterFields ) {
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return notFound( body );
		}
		return status( HttpStatus.NOT_FOUND , body , filterFields );
	}

	public static ResponseEntity< String > unprocessableEntity () {
		return status( HttpStatus.UNPROCESSABLE_ENTITY , HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase() );
	}

	public static < T > ResponseEntity< T > unprocessableEntity ( final T body ) {
		return status( HttpStatus.UNPROCESSABLE_ENTITY , body );
	}

	public static < T > ResponseEntity< T > unprocessableEntity ( final T body , final String filterFields ) {
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return unprocessableEntity( body );
		}
		return status( HttpStatus.UNPROCESSABLE_ENTITY , body , filterFields );
	}

	public static ResponseEntity< String > internalServerError () {
		return status( HttpStatus.INTERNAL_SERVER_ERROR , HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() );
	}

	public static < T > ResponseEntity< T > internalServerError ( final T body ) {
		return status( HttpStatus.INTERNAL_SERVER_ERROR , body );
	}

	public static < T > ResponseEntity< T > internalServerError ( final T body , final String filterFields ) {
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return internalServerError( body );
		}
		return status( HttpStatus.INTERNAL_SERVER_ERROR , body , filterFields );
	}

	public static ResponseEntity< String > badRequest () {
		return status( HttpStatus.BAD_REQUEST , HttpStatus.BAD_REQUEST.getReasonPhrase() );
	}

	public static < T > ResponseEntity< T > badRequest ( final T body ) {
		return status( HttpStatus.BAD_REQUEST , body );
	}

	public static < T > ResponseEntity< T > badRequest ( final T body , final String filterFields ) {
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return badRequest( body );
		}
		return status( HttpStatus.BAD_REQUEST , body , filterFields );
	}

	public static ResponseEntity< String > accepted () {
		return status( HttpStatus.ACCEPTED , HttpStatus.ACCEPTED.getReasonPhrase() );
	}

	public static < T > ResponseEntity< T > accepted ( final T body ) {
		return status( HttpStatus.ACCEPTED , body );
	}

	public static < T > ResponseEntity< T > accepted ( final T body , final String filterFields ) {
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return accepted( body );
		}
		return status( HttpStatus.ACCEPTED , body , filterFields );
	}

	public static ResponseEntity< String > noContent () {
		return status( HttpStatus.NO_CONTENT , HttpStatus.NO_CONTENT.getReasonPhrase() );
	}

	public static < T > ResponseEntity< T > noContent ( final T body ) {
		return status( HttpStatus.NO_CONTENT , body );
	}

	public static < T > ResponseEntity< T > noContent ( final T body , final String filterFields ) {
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return noContent( body );
		}
		return status( HttpStatus.NO_CONTENT , body , filterFields );
	}

	private static ResponseEntity.BodyBuilder buildStatus ( int status ) {
		return ResponseEntity.status( status );
	}

	public static < T > ResponseEntity< T > status ( final int status ) {
		return buildStatus( status ).build();
	}

	public static < T > ResponseEntity< T > status ( final int status , final T body ) {
		return buildStatus( status ).body( body );
	}

	public static < T > ResponseEntity< T > status ( final int status , final T body , final String filterFields ) {
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return status( status , body );
		}
		return status( status , ( T ) jsonToType( toFilterJson( body , filterFields ) , body.getClass() )
		);
	}

	public static < T > ResponseEntity< T > status ( final HttpStatus status ) {
		return status( status.value() );
	}

	public static < T > ResponseEntity< T > status ( final HttpStatus status , final T body ) {
		return status( status.value() , body );
	}

	public static < T > ResponseEntity< T > status ( final HttpStatus status ,
													 final T body ,
													 final String filterFields ) {
		return status( status.value() , body , filterFields );
	}

	public static ResponseEntity< String > ok () {
		return status( HttpStatus.OK , HttpStatus.OK.getReasonPhrase() );
	}


	public static < T > ResponseEntity< T > ok ( final T body ) {
		return status( HttpStatus.OK , body );
	}

	public static < T > ResponseEntity< T > ok ( final T body , final String filterFields ) {
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return ok( body );
		}
		return status( HttpStatus.OK , body , filterFields );
	}

	public static boolean isOk ( ResponseEntity responseEntity ) {
		if ( Objects.isNull( responseEntity ) || Objects.isNull( responseEntity.getStatusCode() ) ) {
			return false;
		}
		return HttpStatus.OK.equals( responseEntity.getStatusCode() );
	}

	public static boolean isNotOk ( ResponseEntity responseEntity ) {
		return ! isOk( responseEntity );
	}

	public static boolean is2xxSuccessful ( ResponseEntity responseEntity ) {
		if ( Objects.isNull( responseEntity ) || Objects.isNull( responseEntity.getStatusCode() ) ) {
			return false;
		}
		return responseEntity.getStatusCode().is2xxSuccessful();
	}

	public static boolean isNot2xxSuccessful ( ResponseEntity responseEntity ) {
		return ! is2xxSuccessful( responseEntity );
	}

	public ResponseEntity< Map< String, Object > > buildNotFound () {
		return ResponseEntityPro.notFound( this.body );
	}

	public ResponseEntity< Map< String, Object > > buildAccepted () {
		return ResponseEntityPro.accepted( this.body );
	}

	public ResponseEntity< Map< String, Object > > buildNoContent () {
		return ResponseEntityPro.noContent( this.body );
	}

	public ResponseEntity< Map< String, Object > > buildUnprocessableEntity () {
		return ResponseEntityPro.unprocessableEntity( this.body );
	}

	public ResponseEntity< Map< String, Object > > buildBadRequest () {
		return ResponseEntityPro.badRequest( this.body );
	}

	public ResponseEntity< Map< String, Object > > buildInternalServerError () {
		return ResponseEntityPro.internalServerError( this.body );
	}

	public ResponseEntity< Map< String, Object > > buildByStatus ( final HttpStatus status ) {
		return ResponseEntityPro.status( status , this.body );
	}

	public ResponseEntity< Map< String, Object > > buildByStatus ( final int status ) {
		return ResponseEntityPro.status( status , this.body );
	}

	public ResponseEntity< Map< String, Object > > buildOk () {
		return ResponseEntityPro.ok( this.body );
	}

	public ResponseEntityPro add ( final String key , final Object value ) {
		if ( MapUtils.isEmpty( this.body ) ) {
			this.body = new HashMap<>( 2 );
			this.body.put( key , value );
			return this;
		}
		this.body.put( key , value );
		return this;
	}

	public ResponseEntityPro flushBodyByFilterFields ( final String filterFields ) {
		if ( MapUtils.isEmpty( this.body ) ) {
			return this;
		}
		if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
			return this;
		}
		this.body = jsonToType( toFilterJson( this.body , filterFields ) , MAP_TYPE_REFERENCE );
		return this;
	}



}





























