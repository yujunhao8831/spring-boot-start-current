package com.goblin.manage.bean.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统配置
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-12-28
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
@TableName( "system_config" )
public class SystemConfig extends Model< SystemConfig > {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId( value = "id", type = IdType.AUTO )
	private Long   id;
	/**
	 * key
	 */
	@TableField( "config_key" )
	private String configKey;
	/**
	 * value
	 */
	@TableField( "config_value" )
	private String configValue;
	/**
	 * 说明
	 */
	@TableField( "config_description" )
	private String configDescription;
	/**
	 * 创建人
	 */
	@TableField( "create_user_id" )
	private Long   createUserId;
	/**
	 * 修改人
	 */
	@TableField( "update_user_id" )
	private Long   updateUserId;
	/**
	 * 创建时间
	 */
	@TableField( "create_time" )
	private Date   createTime;
	/**
	 * 修改时间
	 */
	@TableField( "update_time" )
	private Date   updateTime;
	/**
	 * 备注
	 */
	private String remark;


	@Override
	protected Serializable pkVal () {
		return this.id;
	}

}
