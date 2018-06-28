package com.goblin.response;

import com.goblin.common.ResponseEntityPro;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * @author : 披荆斩棘
 * @date : 2017/11/10
 */
public class ResponseEntityProTest {

	@Test
	public void test1 () {
		User user = new User().setUsername( "披荆斩棘" )
							  .setEmail( "yujunhao_8831@yahoo.com" )
							  .setNickName( "披荆斩棘" )
							  .setPhone( "18696666666" )
							  .setPassword( "123456" )
							  .setPasswordSalt( "-1" )
							  .setEnabled( true );

		final ResponseEntity< User > ok = ResponseEntityPro.ok( user );
		System.err.println( "ok =  " + ok + "\n" );
		final ResponseEntity< User > notFound = ResponseEntityPro.notFound( user );
		System.err.println( "notFound =  " + notFound + "\n" );
		final ResponseEntity< User > accepted = ResponseEntityPro.accepted( user );
		System.err.println( "accepted =  " + accepted + "\n" );
		final ResponseEntity< User > noContent = ResponseEntityPro.noContent( user );
		System.err.println( "noContent =  " + noContent + "\n" );
		final ResponseEntity< User > unprocessableEntity = ResponseEntityPro.unprocessableEntity( user );
		System.err.println( "unprocessableEntity =  " + unprocessableEntity + "\n" );
		final ResponseEntity< User > badRequest = ResponseEntityPro.badRequest( user );
		System.err.println( "badRequest =  " + badRequest + "\n" );
		final ResponseEntity< User > internalServerError = ResponseEntityPro.internalServerError( user );
		System.err.println( "internalServerError =  " + internalServerError + "\n" );
		final ResponseEntity< User > status = ResponseEntityPro.status( 501 , user );
		System.err.println( "status =  " + status + "\n" );

	}

	@Test
	public void test2 () {
		User user = new User().setUsername( "披荆斩棘" )
							  .setEmail( "yujunhao_8831@yahoo.com" )
							  .setNickName( "披荆斩棘" )
							  .setPhone( "18696666666" )
							  .setPassword( "123456" )
							  .setPasswordSalt( "-1" )
							  .setEnabled( true );

		String filterFields = "username,password";

		final ResponseEntity< User > ok = ResponseEntityPro.ok( user , filterFields );
		System.err.println( "ok =  " + ok + "\n" );
		final ResponseEntity< User > notFound = ResponseEntityPro.notFound( user , filterFields );
		System.err.println( "notFound =  " + notFound + "\n" );
		final ResponseEntity< User > accepted = ResponseEntityPro.accepted( user , filterFields );
		System.err.println( "accepted =  " + accepted + "\n" );
		final ResponseEntity< User > noContent = ResponseEntityPro.noContent( user , filterFields );
		System.err.println( "noContent =  " + noContent + "\n" );
		final ResponseEntity< User > unprocessableEntity = ResponseEntityPro.unprocessableEntity( user ,
																								   filterFields );
		System.err.println( "unprocessableEntity =  " + unprocessableEntity + "\n" );
		final ResponseEntity< User > badRequest = ResponseEntityPro.badRequest( user , filterFields );
		System.err.println( "badRequest =  " + badRequest + "\n" );
		final ResponseEntity< User > internalServerError = ResponseEntityPro.internalServerError( user ,
																								   filterFields );
		System.err.println( "internalServerError =  " + internalServerError + "\n" );
		final ResponseEntity< User > status = ResponseEntityPro.status( 501 , user , filterFields );
		System.err.println( "status =  " + status + "\n" );

	}


	@Test
	public void test3 () {
		final ResponseEntity< Map< String, Object > > ok = new ResponseEntityPro().add( "name" , "披荆斩棘" )
																				   .add( "password" , "123456" )
																				   .buildOk();

		System.err.println( "ok = " + ok );

		final ResponseEntity< Map< String, Object > > buildByStatus = new ResponseEntityPro().add( "name" ,
																									"披荆斩棘" )
																							  .add( "password" ,
																									"123456" )
																							  .buildByStatus( 2 );
		System.err.println( "buildByStatus = " + buildByStatus );

		final ResponseEntity< Map< String, Object > > badRequest = new ResponseEntityPro().add( "name" ,
																								 "披荆斩棘" )
																						   .add( "password" ,
																								 "123456" )
																						   .buildBadRequest();

		System.err.println( "badRequest = " + badRequest );

	}


	@Test
	public void test4 () {
		final ResponseEntity< String > ok = ResponseEntityPro.ok();
		System.err.println( "ok = " + ok );

		System.err.println( ResponseEntityPro.isOk( ok ) );
		System.err.println( ResponseEntityPro.isNotOk( ok ) );


		final ResponseEntity< Object > badRequest = ResponseEntity.badRequest().build();
		System.err.println( "badRequest = " + badRequest );

		System.err.println( ResponseEntityPro.isOk( badRequest ) );
		System.err.println( ResponseEntityPro.isNotOk( badRequest ) );


	}

}
















