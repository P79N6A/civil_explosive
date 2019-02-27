package cn.zhiyuan.ces.sys.biz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.sys.biz.IRoleMenuBiz;
import cn.zhiyuan.ces.sys.biz.ISysRoleBiz;
import cn.zhiyuan.ces.sys.entity.RoleMenu;
import cn.zhiyuan.ces.sys.entity.SysRole;
import cn.zhiyuan.frame.BaseDao;


@Service
public class SysRoleBizImpl extends BaseDao<SysRole> implements ISysRoleBiz {

	@Autowired
	private IRoleMenuBiz rolemenuBiz;
	
	
	@Override
	public void saveSysRoleAuth(Integer roleId, List<Integer> menuIdList) {
		RoleMenu rolemenu = new RoleMenu();
		rolemenu.setRoleId(roleId);
		rolemenuBiz.delete(rolemenu);
		for(Integer menuId : menuIdList){
			rolemenu = new RoleMenu();
			rolemenu.setRoleId(roleId);
			rolemenu.setMenuId(menuId);
			rolemenuBiz.add(rolemenu);
		}
	}


	@Override
	public List<SysRole> role4Reg(Integer roleType) {
		String sql = "select * from sys_role where  role_type=:roleType order by order_num asc";
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("roleType", roleType);
		return super.queryList(sql, paramMap);
	}
	
}
