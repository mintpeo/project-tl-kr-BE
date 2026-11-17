package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.UserLevelRes;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.model.UserLevel;
import com.atbm.projecttlkrbe.model.UserLevelEnum;
import com.atbm.projecttlkrbe.repository.UserLevelRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLevelSer {
    private final UserLevelRep rep;
    private final UserRep userRep;

    // Create new user level beginner
    public void setUserLevel(Long userId) {
        User user = userRep.findById(userId).orElse(null);
        if (user == null) return;
        UserLevel userLevel = rep.findByUserId(userId).orElse(null);
        if (userLevel == null) {
            UserLevel newUserLevel = new UserLevel();
            newUserLevel.setLevel(UserLevelEnum.BEGINNER);
            newUserLevel.setUser(user);
            rep.save(newUserLevel);
        } else {
            userLevel.setLevel(UserLevelEnum.ADVANCED);
            userLevel.setUser(user);
            rep.save(userLevel);
        }
    }

    // Get user level
    public UserLevelRes getUserLevelByUserId(Long userId) {
        User user = userRep.findById(userId).orElse(null);
        if (user == null) return null;

        UserLevel userLevel = rep.findByUserId(userId).orElse(null);
        if (userLevel == null) return null;

        UserLevelRes res = new UserLevelRes();
        UserLevelEnum currentLevel = userLevel.getLevel();
        if (UserLevelEnum.BEGINNER.equals(currentLevel)) {
            res.setUserLevel("Cơ bản");
            res.setNextLevelTitle("Nâng cao");
            return res;
        }
        res.setUserLevel("Nâng cao");
        res.setNextLevelTitle(null);
        return res;
    }
}
