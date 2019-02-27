package cn.zhiyuan.ces.sys.biz.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.base.biz.IWorkingUnitBiz;
import cn.zhiyuan.ces.base.entity.WorkingUnit;
import cn.zhiyuan.ces.sys.biz.ISysUserBiz;
import cn.zhiyuan.ces.sys.biz.IUserRoleBiz;
import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.orm.builder.IWhereSql;


@Service
public class SysUserBizImpl extends BaseDao<SysUser> implements ISysUserBiz {

	@Autowired
	private IUserRoleBiz iUserRoleBiz;
	
	@Autowired
	private IWorkingUnitBiz iWorkingUnitBiz;
	
	public int saveUser(SysUser user) {
		if(user.getUserId() == null) {
			super.addAndId(user);
		}else {
			super.update(user);
		}
		return iUserRoleBiz.saveUserRoles(user.getUserId(), user.getRoleIds());
	}
	
	@Override
	public SysUser login(MsgBean mbean, String userName, String password) {
		SysUser user = new SysUser();
		user.setLoginName(userName);
		user = super.get(user);
		if(user != null){
			String pwd1 = CommonUtils.encryptSHA(password);
			String pwd2 = user.getPassWord();
			if(!pwd2.equals(pwd1)) {
				mbean.setRetInfo(-30, "密码输入错误，请重新输入!");
				return null;
			}
			/*Date date = new Date();
			if(date.after(user.getValidDate())) {
				mbean.setRetInfo(-40, "账号已经过期!");
				return null;
			}*/
			if(!new Integer(1).equals(user.getStatus())) {
				mbean.setRetInfo(-40, "账号已经停用!");
				return null;
			}
			SysUser u = new SysUser();
			u.setUserId(user.getUserId());
			u.setLastLoginTime(new Date());//更新登录时间
			super.update(u);
			mbean.setRetInfo(0, "登录成功");
		}else{
			mbean.setRetInfo(-30, "帐号不存在,请重新输入。");
		}
		return user;
	}
	
	/*验证登录帐号是否重复
	 * 即登录帐号记录数 > 0
	 * */
	@Override
	public int checkLoginName(String loginName) {
		String sql = "select count(1) from sys_user t where t.login_name=:login_name ";
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("login_name", loginName);
		return queryObject(sql, paramMap,Integer.class);
	}
	
	@Override
	public List<SysUser> getRegAccounts(String loginName,Integer roleType) {
		String sql = null;
		sql = "select  DISTINCT t1.user_id,t1.login_name,t1.reg_time,t1.last_login_time,t1.status "
				+ ",t1.real_name,t1.real_name2,t3.role_type,t1.unit_id "
				//+ ",(select tt.name from working_unit tt  where tt.id= t1.unit_id) as unit_name "
				+ ",(select group_concat(tt1.role_name) from sys_role tt1,user_role tt2 " + 
				"	where  tt1.role_id = tt2.role_id and tt2.user_id = t1.user_id) as role_names"
				+ ",(select group_concat(tt1.role_id) from sys_role tt1,user_role tt2 " + 
				"	where  tt1.role_id = tt2.role_id and tt2.user_id = t1.user_id) as role_ids"
				//+ ", (select tt1.role_type from sys_role tt1,user_role tt2 " + 
				//"	where  tt1.role_id = tt2.role_id and tt2.user_id = t1.user_id) as user_type "
				+ " from sys_user t1,user_role t2,sys_role t3 "
				+ " where t1.user_id = t2.user_id and t2.role_id = t3.role_id ";
		Map<String,Object> paramMap = new HashMap<>();
		if(roleType != null) {
			sql += " and t3.role_type = :roleType ";
			paramMap.put("roleType", roleType);
		}
		if(StringUtils.isNotEmpty(loginName)) {
			sql += " and t1.login_name like :loginName ";
			paramMap.put("loginName", "%" + loginName + "%");
		}
		return super.queryList(sql, paramMap);
	}
	
	@Override
	public int regAccount(MsgBean mbean,SysUser user) {
		if(user.getUserId() == null){
			if( checkLoginName(user.getLoginName()) > 0) {
				mbean.setRetInfo(-2, SysUser.ERROR_MSG);
				return 0;
			}
		}else user.setLoginName(null);//不准修改登录帐号
		if(user.getStatus() == null) user.setStatus(1);
		user.setRegTime(new Date());
		user.setPassWord(SysUser.DEFAULT_MM);
		if(3 == user.getRoleType()){//从业单位
			WorkingUnit workingUnit = new WorkingUnit();
			workingUnit.setId(user.getUnitId());
			workingUnit.setAuditStatus(3);//审核完毕
			workingUnit.setName(user.getRealName2());//全称
			workingUnit.setShortName(user.getRealName());//简称
			iWorkingUnitBiz.saveWorkingUnit(workingUnit, user.getRoleIds());
			if(user.getUnitId() == null) user.setUnitId(workingUnit.getId());
		}
		saveUser(user);
		return 1;
	}

}
