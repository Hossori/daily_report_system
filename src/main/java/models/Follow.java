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

/*
 * フォローデータのDTOモデル
 */
@Table(name=JpaConst.TABLE_FLW)
@NamedQueries({
    // 指定した従業員がフォローしている全従業員を取得
    @NamedQuery(name=JpaConst.Q_FLW_GET_MINE_FOLLOWING,
                query=JpaConst.Q_FLW_GET_MINE_FOLLOWING_DEF),
    // フォローしている全従業員の日報データを取得
    @NamedQuery(name=JpaConst.Q_FLW_GET_ALL_FOLLOWING,
                query=JpaConst.Q_FLW_GET_ALL_FOLLOWING_DEF),
    // フォローしている全従業員の日報件数を取得
    @NamedQuery(name=JpaConst.Q_FLW_COUNT_ALL_FOLLOWING,
                query=JpaConst.Q_FLW_COUNT_ALL_FOLLOWING_DEF),
    // 従業員,フォローされている従業員を指定して主キーを取得
    @NamedQuery(name=JpaConst.Q_FLW_GET_PRIMARYKEY,
                query=JpaConst.Q_FLW_GET_PRIMARYKEY_DEF)
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Follow {
    @Id
    @Column(name=JpaConst.FLW_COL_ID)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name=JpaConst.FLW_COL_EMP, nullable=false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name=JpaConst.FLW_COL_FLWED, nullable=false)
    private Employee followed;
}
