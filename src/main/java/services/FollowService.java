package services;

import java.util.ArrayList;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import constants.JpaConst;
import models.Employee;

public class FollowService extends ServiceBase {

    /*
     * @param loginEmployee ログイン中の従業員
     * @param employees 従業員のリスト
     * @return employeesがログイン中の従業員にフォローされているか否かのbooleanリスト
     */
    public List<Boolean> getIsFollowList(EmployeeView loginEmployee, List<EmployeeView> employees) {
        List<Boolean> list = new ArrayList<>();

        //ログイン中の従業員がフォローしている従業員のリストを取得
        List<EmployeeView> followedEmployees =
            EmployeeConverter.toViewList(
                em.createNamedQuery(JpaConst.Q_FLW_GET_MINE_FOLLOWING, Employee.class)
                                    .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(loginEmployee))
                                    .getResultList()
            );

        for(EmployeeView ev:employees) {
            list.add(isFollow(followedEmployees, ev));
        }

        return list;
    }

    /*
     * @param loginEmployee ログイン中の従業員
     * @param ev この従業員がフォローされているか調べる
     * @return フォローされているか否かのboolean値
     */
    public Boolean isFollow(List<EmployeeView> followedEmployees, EmployeeView ev) {
        Boolean flag = false;

        for(EmployeeView employee:followedEmployees) {
            if(employee == ev) { //フォローリストにいればtrueを返す
                flag = true;
                return flag;
            }
        }

        //フォローリストにいなければfalseを返す
        return flag;
    }
}
