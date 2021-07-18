package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import services.EmployeeService;
import services.FollowService;

public class FollowAction extends ActionBase {
    private FollowService service;
    private EmployeeService employeeService;

    @Override
    public void process() throws ServletException, IOException {
        service = new FollowService();
        employeeService = new EmployeeService();

        invoke();

        service.close();
        employeeService.close();
    }

    public void index() throws ServletException, IOException {
        // 指定したページに表示される従業員のリストを作成する
        //ページを取得
        int page = getPage();
        //全従業員データを取得
        List<EmployeeView> employees = employeeService.getPerPage(page);

        // 従業員のリストに対応させる形で、ログイン中の従業員がフォローしているかどうかのリストを作成する
        //セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        //フォローしているかどうかのリストを作成
        List<Boolean> isFollowList = service.getIsFollowList(loginEmployee, employees);

        putRequestScope(AttributeConst.EMPLOYEES, employees);
        putRequestScope(AttributeConst.FLW_IS_FOLLOW_LIST, isFollowList);
        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        //セッションスコープにフラッシュが入っていた場合、リクエストスコープに移し替える
        if(getSessionScope(AttributeConst.FLUSH) != null) {
            putRequestScope(AttributeConst.FLUSH, getSessionScope(AttributeConst.FLUSH));
            removeSessionScope(AttributeConst.FLUSH);
        }

        forward(ForwardConst.FW_FLW_INDEX);
    }

    public void create() throws ServletException, IOException {

        if(checkToken() == true) {
            //ログイン中の従業員を取得する
            EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //idを条件に従業員データを取得する
            EmployeeView ev = employeeService.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

            //データが取得できなかった、または論理削除されている場合はエラー画面を表示
            if (ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;
            }

            //create操作を実行する
            service.create(loginEmployee, ev);
            //フラッシュ
            putSessionScope(AttributeConst.FLUSH, ev.getName() + " をフォローしました。");

            redirect(ForwardConst.ACT_FLW, ForwardConst.CMD_INDEX);
        }
    }

    public void destroy() throws ServletException, IOException {

        if(checkToken() == true) {
            //ログイン中の従業員を取得する
            EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
            //フォロー解除する従業員を取得する
            EmployeeView ev = employeeService.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

            //データが取得できなかった、または論理削除されている場合はエラー画面を表示
            if (ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;
            }

            //destroy操作を実行する
            service.destroy(loginEmployee, ev);
            //フラッシュ
            putSessionScope(AttributeConst.FLUSH, ev.getName() + " のフォローを解除しました。");

            redirect(ForwardConst.ACT_FLW, ForwardConst.CMD_INDEX);
        }
    }


}
