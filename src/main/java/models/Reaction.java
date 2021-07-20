package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name=JpaConst.TABLE_REACT)
@NamedQueries({ //適当にクエリいれておく
    @NamedQuery(name=JpaConst.Q_REACT_GET_REPORT_REACTION,
                query=JpaConst.Q_REACT_GET_REPORT_REACTION_DEF)
})

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reaction {

    @Id
    @Column(name=JpaConst.REACT_COL_ID)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    //従業員
    @ManyToOne
    @JoinColumn(name=JpaConst.REACT_COL_EMP)
    private Employee employee;

    //リアクションしたレポート
    @ManyToOne
    @JoinColumn(name=JpaConst.REACT_COL_REP)
    private Report report;

    //リアクションタイプ
    @Column(name=JpaConst.REACT_COL_REACT_TYPE)
    private Integer reactionType;

}
