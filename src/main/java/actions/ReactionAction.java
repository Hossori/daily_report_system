package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import models.Reaction;
import services.ReactionService;
import services.ReportService;

public class ReactionAction extends ActionBase {

    private ReportService reportService;
    private ReactionService service;

    @Override
    public void process() throws ServletException, IOException {
        reportService = new ReportService();
        service = new ReactionService();

        invoke();

        reportService.close();
        service.close();
    }

    public void create() throws ServletException, IOException {

        if(checkToken()) {

            EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
            ReportView rv = reportService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));
            Integer reactionType = Integer.parseInt(getRequestParam(AttributeConst.REACT_TYPE));
            String[] reactionMsg = {"いいね","賞賛"};

            if (rv == null) {
                //該当の日報データが存在しない場合はエラー画面を表示
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;
            }

            if(reportService.isReaction(loginEmployee, rv, reactionType)) {
                forward(ForwardConst.FW_ERR_UNKNOWN);
            } else {
                Reaction re = new Reaction(
                                        null,
                                        EmployeeConverter.toModel(loginEmployee),
                                        ReportConverter.toModel(rv),
                                        reactionType
                                    );
                service.create(re);
                putRequestScope(AttributeConst.FLUSH, reactionMsg[reactionType]+"しました。");

                redirectReportShow(rv, loginEmployee);
            }
        }
    }

    public void destroy() throws ServletException, IOException {

        if(checkToken()) {

            EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
            ReportView rv = reportService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));
            Integer reactionType = Integer.parseInt(getRequestParam(AttributeConst.REACT_TYPE));
            String[] reactionMsg = {"いいね","賞賛"};

            if(rv == null || !(reportService.isReaction(loginEmployee, rv, reactionType))) {
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;
            } else {
                service.destroy(loginEmployee, rv, reactionType);
                putRequestScope(AttributeConst.FLUSH, reactionMsg[reactionType]+"を取り消しました。");
                System.out.println(1);
                redirectReportShow(rv, loginEmployee);
                System.out.println(2);
            }
        }
    }

    /* 直接reportのshowメソッドにリダイレクトしようとすると何故かエラーになってしまうので、
     * reportのshowメソッド内の処理をこちらに記述
     */
    private void redirectReportShow(ReportView rv, EmployeeView loginEmployee) throws ServletException, IOException {
        Integer goodReaction = reportService.getReactionCount(rv, JpaConst.REACT_TYPE_GOOD);
        Integer praiseReaction = reportService.getReactionCount(rv, JpaConst.REACT_TYPE_PRAISE);
        putRequestScope(AttributeConst.REACT_GOOD, goodReaction);
        putRequestScope(AttributeConst.REACT_PRAISE, praiseReaction);
        putRequestScope(AttributeConst.TOKEN, getTokenId()); //リアクションのクリエイト,デストロイ用

        //リアクション済みかどうか
        Boolean isGoodReaction = reportService.isReaction(loginEmployee, rv, JpaConst.REACT_TYPE_GOOD);
        Boolean isPraiseReaction = reportService.isReaction(loginEmployee, rv, JpaConst.REACT_TYPE_PRAISE);
        putRequestScope(AttributeConst.IS_GOOD_REACT, isGoodReaction);
        putRequestScope(AttributeConst.IS_PRAISE_REACT, isPraiseReaction);

        putRequestScope(AttributeConst.REPORT, rv); //取得した日報データ
        //詳細画面を表示
        forward(ForwardConst.FW_REP_SHOW);
    }

}
