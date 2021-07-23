package services;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Employee;
import models.Reaction;
import models.Report;

public class ReactionService extends ServiceBase {

    public void create(Reaction re) {
        createInternal(re);
    }

    private void createInternal(Reaction re) {
        em.getTransaction().begin();
        em.persist(re);
        em.getTransaction().commit();
    }

    public void destroy(EmployeeView loginEmployee, ReportView rv, Integer reactionType) {
        Employee e = EmployeeConverter.toModel(loginEmployee);
        Report r = ReportConverter.toModel(rv);
        Reaction re = em.createNamedQuery(JpaConst.Q_REACT_GET_REACTION, Reaction.class)
                            .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, e)
                            .setParameter(JpaConst.JPQL_PARM_REPORT, r)
                            .setParameter(JpaConst.JPQL_PARM_REACT_TYPE, reactionType)
                            .getSingleResult();

        destroyInternal(re);
    }

    private void destroyInternal(Reaction re) {
        em.getTransaction().begin();
        em.remove(re);
        em.getTransaction().commit();
    }
}
