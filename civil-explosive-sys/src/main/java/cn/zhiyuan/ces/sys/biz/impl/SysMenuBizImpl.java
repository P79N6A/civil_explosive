package cn.zhiyuan.ces.sys.biz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.sys.biz.ISysMenuBiz;
import cn.zhiyuan.ces.sys.entity.SysMenu;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;


@Service
public class SysMenuBizImpl extends BaseDao<SysMenu> implements ISysMenuBiz {

	@Override
	public List<SysMenu> queryMenuByRoleId(Integer userId) {
		String sql = "select  DISTINCT t1.*,"
				+ "(select t3.code_name from sys_code t3  "
				+ " where t3.code_value = t1.menu_type  and  t3.code_type = 2 limit 1) as menu_type_name "
				+ " from sys_menu t1,role_menu t2,user_role t3 "
					+ "where t1.menu_id = t2.menu_id and t2.role_id = t3.role_id "
					+ "and t3.user_id = :userId  order by t1.order_num asc ";	
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		return super.queryList(sql, paramMap);
	}


	@Override
	public int save(SysMenu sysMenu) {
		int ret = 0;
		if(sysMenu.getMenuId() == null) {
			ret = super.addAndId(sysMenu);
			SysMenu menu = new SysMenu();
			menu.setMenuId(sysMenu.getMenuId());
			menu.setOrderNum(sysMenu.getMenuId());
			super.update(menu);
		}else {
			//更新 or 新增后更新ordernum
			ret = super.update(sysMenu);
		}
		return ret;
	}
	
	@Override
	public int orderSysMenu(MsgBean mbean, SysMenu menu1, SysMenu menu2) {
		Integer order = menu1.getOrderNum();
		menu1.setOrderNum(menu2.getOrderNum());
		menu2.setOrderNum(order);
		super.update(menu1);
		super.update(menu2);
		return 0;
	}


	@Override
	public void ormtest() {
		testSelect();
	}
	
	private void testSelect() {
		SysMenu menu = new SysMenu();
		menu.setMenuWidth(800);
		//menu.setRemark("66");
		PageBean<SysMenu> pb = new PageBean<>();
		pb.setPageNum(3);
		pb.setPageSize(16);
		List<SysMenu> menulist = super.getPageList(pb,menu, null);
		log.debug(menulist.size());
		log.debug(menulist);
		log.debug(pb);
	}
}
