package cn.zhiyuan.ces.sys.biz.impl;

import cn.zhiyuan.ces.sys.entity.UserRole;
import cn.zhiyuan.ces.sys.biz.IUserRoleBiz;

import java.util.List;

import org.springframework.stereotype.Service;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;


@Service
public class UserRoleBizImpl extends BaseDao<UserRole> implements IUserRoleBiz {

	@Override
	public int saveUserRoles(Integer userId, String roleIdStrs) {
		List<Integer> roleIds = CommonUtils.str2IntList(roleIdStrs);
		if(roleIds.isEmpty()) return 0;
		UserRole userrole = new UserRole();
		userrole.setUserId(userId);
		super.delete(userrole);//清空
		for(Integer roleId : roleIds) {
			userrole.setRoleId(roleId);
			super.add(userrole);
		}
		return 1;
	}

}
