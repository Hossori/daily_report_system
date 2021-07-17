package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@NamedQueries({ //ひとまず適当にクエリいれた
    @NamedQuery(name="none",
                query="SELECT f FROM Follow AS f")
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

    @Column(name=JpaConst.FLW_COL_FLWER)
    private Employee follower;

    @Column(name=JpaConst.FLW_COL_FLWED)
    private Employee followed;
}
