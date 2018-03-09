package com.goblin.common.util;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 递归工具
 *
 * @author : 披荆斩棘
 * @date : 2017/7/7
 */
public final class RecursionUtils {

	/**
	 * 递归得到所有父子主键
	 *
	 * @param models      implements {@link ParentChildrenRecursion}
	 * @param idContainer 存储id
	 * @return 父子主键
	 */
	public static List< Serializable > listIds ( List< ? extends ParentChildrenRecursion > models ,
												 List< Serializable > idContainer ) {
		if ( CollectionUtils.isEmpty( models ) ) {
			return idContainer;
		}
		for ( ParentChildrenRecursion model : models ) {
			idContainer.add( model.getId() );
			if ( CollectionUtils.isNotEmpty( model.getChildren() ) ) {
				listIds( model.getChildren() , idContainer );
			}
		}
		return idContainer;
	}

	/**
	 * list转换为有层级关系的list,默认从顶级开始
	 * <pre>
	 *
	 *   before :
	 *   element | element | element | element | element | element
	 *
	 *   after :
	 *                          root
	 *   children | children children | children children children
	 *   ... ... ... ... ... ... ... ... ... ... ... ... ... ... ...
	 *   RecursionUtils.listToTree(list);
	 *   [
	 *      {
	 *          "id": 1,
	 *          "parentId": 0,
	 *          "permissionName": "角色管理",
	 *          "children": [{
	 *              "id": 2,
	 *              "parentId": 1,
	 *              "permissionName": "角色列表",,
	 *              "children": [{
	 *                     "id": 3,
	 *                     "parentId": 2,
	 *                     "permissionName": "修改角色",
	 *                     "children": []
	 *                }]
	 *           }]
	 *      }
	 *   ]
	 *
	 * </pre>
	 *
	 * @param list implements {@link ParentChildrenRecursion}
	 * @return 有层级关系list
	 */
	public static List listToTree ( List< ? extends ParentChildrenRecursion > list ) {
		return tree( list.get( 0 ).rootId() , swap( list ) );
	}

	/**
	 * {@link #listToTree(List)},指定从某个层级开始
	 * <pre>
	 *   RecursionUtils.listToTree(list);
	 *   [
	 *      {
	 *          "id": 1,
	 *          "parentId": 0,
	 *          "permissionName": "角色管理",
	 *          "children": [{
	 *              "id": 2,
	 *              "parentId": 1,
	 *              "permissionName": "角色列表",,
	 *              "children": [{
	 *                     "id": 3,
	 *                     "parentId": 2,
	 *                     "permissionName": "修改角色",
	 *                     "children": []
	 *                }]
	 *           }]
	 *      },
	 *   ]
	 *   RecursionUtils.listToTree(list,1);
	 *      [
	 *          {
	 *              "id": 2,
	 *               "parentId": 1,
	 *               "permissionName": "角色列表",
	 *               "children": [{
	 *                  "id": 3,
	 *                   "parentId": 2,
	 *                   "permissionName": "修改角色",
	 *                   "children": []
	 *              }]
	 *          }
	 *      ]
	 * </pre>
	 */
	public static List listToTree ( List< ? extends ParentChildrenRecursion > list ,
									Serializable parentId ) {
		return tree( parentId , swap( list ) );
	}

	private static Map< Serializable, List< ParentChildrenRecursion > > swap ( List< ? extends ParentChildrenRecursion > list ) {
		if ( CollectionUtils.isEmpty( list ) ) {
			return Collections.emptyMap();
		}
		Map< Serializable, List< ParentChildrenRecursion > > content = new HashMap<>();
		list.forEach( element -> {
			List resources = content.get( element.getParentId() );
			if ( CollectionUtils.isEmpty( resources ) ) {
				resources = new ArrayList<>();
			}
			resources.add( element );
			content.put( element.getParentId() , resources );
		} );
		return content;
	}


	/**
	 * @see #listToTree(List , Serializable)
	 */
	public static List treeInnerFindParent ( List< ? extends ParentChildrenRecursion > list ,
											 Serializable parentId ) {
		if ( CollectionUtils.isEmpty( list ) ) {
			return Collections.EMPTY_LIST;
		}
		for ( ParentChildrenRecursion element : list ) {
			if ( Objects.equals( element.getParentId() , parentId ) ) {
				return list;
			}
			return treeInnerFindParent( element.getChildren() , parentId );
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * <pre>
	 *     RecursionUtils.treeInnerFindOneself(list,0L) = null
	 * </pre>
	 */
	public static < T extends ParentChildrenRecursion > T treeInnerFindOneself ( List< ? extends ParentChildrenRecursion > list ,
																				 Serializable oneselfId ) {
		if ( CollectionUtils.isEmpty( list ) ) {
			return null;
		}
		for ( ParentChildrenRecursion element : list ) {
			if ( Objects.equals( element.getId() , oneselfId ) ) {
				return ( T ) element;
			}
			final T oneself = innerFindOneself( Collections.singletonList( element ) , oneselfId );
			if ( Objects.nonNull( oneself ) ) {
				return oneself;
			}
		}
		return null;
	}

	public static < T extends ParentChildrenRecursion > T innerFindOneself ( List< ? extends ParentChildrenRecursion > list ,
																			 Serializable oneselfId ) {
		if ( CollectionUtils.isEmpty( list ) ) {
			return null;
		}
		if ( Objects.equals( list.get( 0 ).rootId() , oneselfId ) ) {
			return null;
		}
		for ( ParentChildrenRecursion element : list ) {
			if ( Objects.equals( element.getId() , oneselfId ) ) {
				return ( T ) element;
			}
			final List< ? extends ParentChildrenRecursion > children = element.getChildren();
			if ( CollectionUtils.isNotEmpty( children ) ) {
				return innerFindOneself( children , oneselfId );
			}
		}
		return null;
	}


	/**
	 * 规整
	 *
	 * @param parentId : 上级ID
	 * @param content
	 * @return
	 */
	private static List< ? extends ParentChildrenRecursion > tree ( Serializable parentId ,
																	Map< Serializable, List< ParentChildrenRecursion > > content ) {
		List< ParentChildrenRecursion > container = new ArrayList<>();
		List< ParentChildrenRecursion > children  = content.get( parentId );
		if ( CollectionUtils.isNotEmpty( children ) ) {
			children.forEach( element -> {
				element.setChildren( tree( element.getId() , content ) );
				container.add( element );
			} );
		}
		return container;
	}


	/**
	 * 层级关系的list转换为平行的list
	 * <p>
	 * 现在是一个层级关系的 树形 结构
	 * <pre>
	 *                          root
	 *   children | children children | children children children
	 *   ... ... ... ... ... ... ... ... ... ... ... ... ... ... ...
	 *   转换为平行的,也就是普通的链表
	 *   element | element | element | element | element | element
	 * </pre>
	 *
	 * @param trees     implements {@link ParentChildrenRecursion}
	 * @param container 存储
	 * @return
	 */
	public static List elementTreeToList ( List< ? extends ParentChildrenRecursion > trees ,
										   List< ParentChildrenRecursion > container ) {
		if ( CollectionUtils.isEmpty( trees ) ) {
			return container;
		}
		for ( ParentChildrenRecursion tree : trees ) {
			container.add( tree );
			if ( CollectionUtils.isNotEmpty( tree.getChildren() ) ) {
				elementTreeToList( tree.getChildren() , container );
			}
		}
		// 清空 children 内容
		return container.parallelStream()
						.map( element -> element.setChildren( null ) )
						.collect( Collectors.toList() );
	}

	/**
	 * @see #elementTreeToList(List , List)
	 */
	public static List elementTreeToList ( ParentChildrenRecursion tree ,
										   List< ParentChildrenRecursion > container ) {
		return elementTreeToList( Arrays.asList( tree ) , container );
	}

	/**
	 * 找到这个列表中,这个元素的所有子元素
	 *
	 * @param list          : 这个元素的关系列表
	 * @param thisElementId : 这个元素的主键
	 * @return
	 */
	public static List findListInnerThisAllChildrenToList ( List< ? extends ParentChildrenRecursion > list ,
															Serializable thisElementId ) {
		// 1. 组织树形结构关系
		final List trees = listToTree( list );
		// 2. 在这个树形结构中,找到自己及以下的树形关系
		final ParentChildrenRecursion thisElementTree = treeInnerFindOneself( trees , thisElementId );
		// 3. 变成一个list
		return elementTreeToList( thisElementTree , new ArrayList<>() );
	}


	public interface ParentChildrenRecursion < T > {

		/**
		 * root ID 默认是0 Long类型
		 * <p>
		 * <b>注意 : 如果实体主键 {@link #getId()} 是Integer 类型</b>,那么你需要自行重写该方法,返回0,而不是0L
		 *
		 * @return default 0L
		 */
		default Serializable rootId () {
			return 0L;
		}

		/**
		 * 主键
		 *
		 * @return id
		 */
		Serializable getId ();

		/**
		 * 父级主键
		 *
		 * @return
		 */
		Serializable getParentId ();

		/**
		 * 子
		 *
		 * @return
		 */
		List< T > getChildren ();

		T setChildren ( List< T > children );


	}
}
