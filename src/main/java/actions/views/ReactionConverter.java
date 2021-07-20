package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Reaction;

public class ReactionConverter {

    public static Reaction toModel(ReactionView rev) {
        return new Reaction(
                    rev.getId(),
                    EmployeeConverter.toModel(rev.getEmployee()),
                    ReportConverter.toModel(rev.getReport()),
                    rev.getReactionType()
                );
    }

    public static ReactionView toView(Reaction re) {
        return new ReactionView(
                re.getId(),
                EmployeeConverter.toView(re.getEmployee()),
                ReportConverter.toView(re.getReport()),
                re.getReactionType()
                );
    }

    public static List<ReactionView> toViewList(List<Reaction> res) {
        List<ReactionView> revs = new ArrayList<>();

        for(Reaction re:res) {
            revs.add(toView(re));
        }

        return revs;
    }

    public static List<Reaction> toModelList(List<ReactionView> revs) {
        List<Reaction> res = new ArrayList<>();

        for(ReactionView rev:revs) {
            res.add(toModel(rev));
        }

        return res;
    }
}
