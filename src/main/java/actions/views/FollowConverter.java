package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Follow;

public class FollowConverter {
    public static Follow toModel(FollowView fv) {
        return new Follow(
                    fv.getId(),
                    EmployeeConverter.toModel(fv.getFollower()),
                    EmployeeConverter.toModel(fv.getFollowed())
                );
    }

    public static FollowView toView(Follow f) {
        if(f == null) {
            return null;
        }

        return new FollowView(
                    f.getId(),
                    EmployeeConverter.toView(f.getFollower()),
                    EmployeeConverter.toView(f.getFollowed())
                );
    }

    public static List<FollowView> toViewList(List<Follow> list) {
        List<FollowView> fvs = new ArrayList<>();

        for(Follow f:list) {
            fvs.add(toView(f));
        }

        return fvs;
    }

    public static void CopyViewToModel(Follow f, FollowView fv) {
        f.setId(fv.getId());
        f.setFollower(EmployeeConverter.toModel(fv.getFollower()));
        f.setFollowed(EmployeeConverter.toModel(fv.getFollowed()));
    }

    public static void CopyModelToView(Follow f, FollowView fv) {
        fv.setId(f.getId());
        fv.setFollower(EmployeeConverter.toView(f.getFollower()));
        fv.setFollowed(EmployeeConverter.toView(f.getFollowed()));
    }
}
