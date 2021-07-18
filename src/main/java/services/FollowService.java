package services;

import java.util.ArrayList;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FollowConverter;
import actions.views.FollowView;
import constants.JpaConst;
import models.Employee;
import models.Follow;

public class FollowService extends ServiceBase {

    /*
     * @param loginEmployee ログイン中の従業員
     * @param employees 従業員のリスト
     * @return employeesがログイン中の従業員にフォローされているか否かのbooleanリスト
     */
    public List<Boolean> getIsFollowList(EmployeeView loginEmployee, List<EmployeeView> employees) {
        List<Boolean> list = new ArrayList<>();

        //ログイン中の従業員がフォローしている従業員のリストを取得
        List<EmployeeView> followedEmployees = getFollowedEmployees(loginEmployee);

        //employeesがloginEmployeeにフォローされているかどうかのboolean値を追加していく
        for(EmployeeView ev:employees) {
            list.add(isFollow(followedEmployees, ev));
        }

        return list;
    }

    /*
     * @param loginEmployee ログイン中の従業員
     * @return loginEmployeeがフォローしている従業員のリスト
     */
    private List<EmployeeView> getFollowedEmployees(EmployeeView loginEmployee) {
        Employee e = EmployeeConverter.toModel(loginEmployee);

        List<Employee> followedEmployees =
                em.createNamedQuery(JpaConst.Q_FLW_GET_MINE_FOLLOWING, Employee.class)
                                    .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, e)
                                    .getResultList();

        return EmployeeConverter.toViewList(followedEmployees);
    }

    /*
     * @param loginEmployee ログイン中の従業員
     * @param ev この従業員がフォローされているか調べる
     * @return フォローされているか否かのboolean値
     */
    private Boolean isFollow(List<EmployeeView> followedEmployees, EmployeeView ev) {
        Boolean flag = false;

        for(EmployeeView employee:followedEmployees) {
            if(employee.getId() == ev.getId()) { //フォローリストにいればtrueを返す
                flag = true;
                return flag;
            }
        }

        //フォローリストにいなければfalseを返す
        return flag;
    }

    /*
     * @param loginEmployee ログイン中の従業員
     * @param ev フォローする従業員
     * フォローテーブルに追加する
     */
    public void create(EmployeeView loginEmployee, EmployeeView ev) {
        FollowView fv = new FollowView();
        fv.setEmployee(loginEmployee);
        fv.setFollowed(ev);
        create(fv);
    }

    /*
     * @param fv フォローのビューオブジェクト
     * モデル化してテーブルに追加する
     */
    private void create(FollowView fv) {
        em.getTransaction().begin();
        em.persist(FollowConverter.toModel(fv));
        em.getTransaction().commit();
    }

    public void destroy(EmployeeView loginEmployee, EmployeeView ev) {
        Follow f =
            em.createNamedQuery(JpaConst.Q_FLW_GET_PRIMARYKEY, Follow.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(loginEmployee))
                .setParameter(JpaConst.JPQL_PARM_FOLLOWED, EmployeeConverter.toModel(ev))
                .getSingleResult();

        destroy(f);
    }

    public void destroy(Follow f) {
        em.getTransaction().begin();
        em.remove(f);
        em.getTransaction().commit();
    }
}
