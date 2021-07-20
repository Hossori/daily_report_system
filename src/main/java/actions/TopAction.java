package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.ReportService;

/**
 * トップページに関する処理を行うActionクラス
 *
 */
public class TopAction extends ActionBase {

    private ReportService service;

    /**
     * indexメソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();

        //メソッドを実行
        invoke();

        service.close();

    }

    /**
     * 一覧画面を表示する
     */
    public void index() throws ServletException, IOException {

        // セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //ページを取得
        int page = getPage();
        //日報のリスト,件数を代入するための変数を定義
        List<ReportView> reports = null;
        long myReportsCount = 0;
        String target = getRequestParam(AttributeConst.TOP_TARGET);

        if(target == null) {
            // targetにつき指定なしの場合

            // ログイン中の従業員が作成した日報データについて、
            // => 指定されたページ数の一覧画面に表示する分取得する
            reports = service.getMinePerPage(loginEmployee, page);
            // => 件数を取得する
            myReportsCount = service.countAllMine(loginEmployee);
        } else if(target.equals(ForwardConst.TOP_TARGET_FOLLOWING.getValue())) {
            // targetがfollowingの場合

            // ログイン中の従業員がフォローしている従業員の日報データについて、
            // => 指定されたページ数の一覧画面に表示する分取得する
            reports = service.getFollowingPerPage(loginEmployee, page);
            // => 件数を取得する
            myReportsCount = service.countAllFollowing(loginEmployee);
        } else {
            // targetの値が無効な場合
            forward(ForwardConst.FW_ERR_UNKNOWN);
        }

        //リアクションリストを格納
        List<Integer> goodReactions = service.getReactionCounts(reports, JpaConst.REACT_TYPE_GOOD);
        List<Integer> praiseReactions = service.getReactionCounts(reports, JpaConst.REACT_TYPE_PRAISE);

        putRequestScope(AttributeConst.REPORTS, reports); // 取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT, myReportsCount); // ログイン中の従業員が作成した日報の数
        putRequestScope(AttributeConst.PAGE, page); // ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); // 1ページに表示するレコードの数
        putRequestScope(AttributeConst.TOP_TARGET, target); // targetの値

        putRequestScope(AttributeConst.REACT_GOOD_LIST, goodReactions); //いいねリアクション
        putRequestScope(AttributeConst.REACT_PRAISE_LIST, praiseReactions); //賞賛リアクション

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_TOP_INDEX);
    }

}