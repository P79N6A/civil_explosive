package cn.zhiyuan.ces.sys.biz;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cn.zhiyuan.ces.sys.entity.SysMenu;
import cn.zhiyuan.frame.IAbstractDao;
import cn.zhiyuan.frame.MsgBean;


public interface ISysMenuBiz extends IAbstractDao<SysMenu> {

	//获取用户包含的所有权限菜单
	List<SysMenu> queryMenuByRoleId(Integer userId);

	//调整菜单顺序
	@Transactional
	int orderSysMenu(MsgBean mbean, SysMenu menu1, SysMenu menu2);

	@Transactional
	void ormtest();
}
