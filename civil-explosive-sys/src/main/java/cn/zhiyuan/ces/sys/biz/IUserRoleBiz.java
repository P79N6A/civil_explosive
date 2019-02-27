package cn.zhiyuan.ces.sys.biz;

import cn.zhiyuan.ces.sys.entity.UserRole;
import cn.zhiyuan.frame.IAbstractDao;


public interface IUserRoleBiz extends IAbstractDao<UserRole> {

	int saveUserRoles(Integer userId,String roleIds);
}
