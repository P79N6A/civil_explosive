package cn.zhiyuan.ces.sys.biz;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.frame.IAbstractDao;
import cn.zhiyuan.frame.MsgBean;


public interface ISysUserBiz extends IAbstractDao<SysUser> {

	int saveUser(SysUser user);
	
	//用户登录
	SysUser login(MsgBean mbean, String userName, String password);

	int checkLoginName(String loginName);
	
	List<SysUser> getRegAccounts(String loginName,Integer roleType);

	//注册账号
	@Transactional
	int regAccount(MsgBean mbean,SysUser user);
}
