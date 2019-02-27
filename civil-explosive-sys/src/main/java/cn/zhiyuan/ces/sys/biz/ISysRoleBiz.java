package cn.zhiyuan.ces.sys.biz;

import java.util.List;

import cn.zhiyuan.ces.sys.entity.SysRole;
import cn.zhiyuan.frame.IAbstractDao;


public interface ISysRoleBiz extends IAbstractDao<SysRole> {

	//角色授权
	void saveSysRoleAuth(Integer roleId, List<Integer> str2IntList);

	List<SysRole> role4Reg(Integer roleType);
}
