package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Employee;
import models.Reaction;
import models.Report;
import models.validators.ReportValidator;

/**
 * 日報テーブルの操作に関わる処理を行うクラス
 */
public class ReportService extends ServiceBase {

    /**
     * 指定した従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得しReportViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getMinePerPage(EmployeeView employee, int page) {

        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL_MINE, Report.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }

    /**
     * 指定した従業員がフォローしている従業員の日報データを、
     * 指定されたページ数の一覧画面に表示する分取得しReportViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getFollowingPerPage(EmployeeView employee, int page) {

        List<Report> reports = em.createNamedQuery(JpaConst.Q_FLW_GET_ALL_FOLLOWING, Report.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }

    /**
     * 指定した従業員が作成した日報データの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定した従業員がフォローしている従業員の日報データの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllFollowing(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_FLW_COUNT_ALL_FOLLOWING, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定されたページ数の一覧画面に表示する日報データを取得し、ReportViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getAllPerPage(int page) {
        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL, Report.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        System.out.println("getAllPerPage NamedQuery => OK");
        for(Report r:reports) {
            System.out.println("id"+r.getId());
            for(Reaction re:r.getReactionList()) {
                System.out.println(re.getReactionType());
            }
        }
        System.out.println("convert");
        List<ReportView> rvs = ReportConverter.toViewList(reports);
        System.out.println("convert_ok");
        return rvs;
    }

    /*
     * 指定した種類のリアクション数を、レポートリストに対応させたリストで返却
     * @param reports レポートのリスト
     * @param reactionType カウントするリアクションの種類
     * @return リアクション数のリスト
     */
    public List<Integer> getReactionCounts(List<ReportView> reports, Integer reactionType) {
        List<Integer> list = new ArrayList<>();

        for(ReportView rv:reports) {
            Integer count;
            count = getReactionCount(rv, reactionType);

            list.add(count);
        }

        return list;
    }

    /*
     * レポートの指定した種類のリアクション数を返却
     * @param rv レポートのビュー
     * @param reactionType カウントするリアクションの種類
     * @return リアクション数
     */
    public Integer getReactionCount(ReportView rv, Integer reactionType) {
        Integer count = 0;

        List<Reaction> reactionList = rv.getReactionList();

        for(Reaction re:reactionList) {
            if(re.getReactionType().equals(reactionType)) {
                count++;
            }
        }

        return count;
    }

    /*
     * ログイン中の従業員がレポートに指定した種類のリアクションをしているかどうか判定
     * @param loginEmployee ログイン中の従業員
     * @param rv 対象のレポート
     * @param reactionType リアクションの種類
     * @return リアクション済みかどうかのboolean値
     */
    public Boolean isReaction(EmployeeView loginEmployee, ReportView rv, Integer reactionType) {
        Employee e = EmployeeConverter.toModel(loginEmployee);
        Report r = ReportConverter.toModel(rv);

        Long count = em.createNamedQuery(JpaConst.Q_REACT_IS_REACT, Long.class)
                                            .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, e)
                                            .setParameter(JpaConst.JPQL_PARM_REPORT, r)
                                            .setParameter(JpaConst.JPQL_PARM_REACT_TYPE, reactionType)
                                            .getSingleResult();

        if(0 == count) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 日報テーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll() {
        long reports_count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT, Long.class)
                .getSingleResult();
        return reports_count;
    }

    /**
     * idを条件に取得したデータをReportViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public ReportView findOne(int id) {
        return ReportConverter.toView(findOneInternal(id));
    }

    /**
     * 画面から入力された日報の登録内容を元にデータを1件作成し、日報テーブルに登録する
     * @param rv 日報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(ReportView rv) {
        List<String> errors = ReportValidator.validate(rv);
        if (errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            rv.setCreatedAt(ldt);
            rv.setUpdatedAt(ldt);
            createInternal(rv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された日報の登録内容を元に、日報データを更新する
     * @param rv 日報の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(ReportView rv) {

        //バリデーションを行う
        List<String> errors = ReportValidator.validate(rv);

        if (errors.size() == 0) {

            //更新日時を現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();
            rv.setUpdatedAt(ldt);

            updateInternal(rv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Report findOneInternal(int id) {
        return em.find(Report.class, id);
    }

    /**
     * 日報データを1件登録する
     * @param rv 日報データ
     */
    private void createInternal(ReportView rv) {

        em.getTransaction().begin();
        em.persist(ReportConverter.toModel(rv));
        em.getTransaction().commit();

    }

    /**
     * 日報データを更新する
     * @param rv 日報データ
     */
    private void updateInternal(ReportView rv) {

        em.getTransaction().begin();
        Report r = findOneInternal(rv.getId());
        ReportConverter.copyViewToModel(r, rv);
        em.getTransaction().commit();

    }

}