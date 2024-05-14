package com.example.demo.module.user.dto.result;

import com.example.demo.module.review.dto.result.ReviewSimpleMyPageResult;
import com.example.demo.module.userStorage.dto.result.UserStorageSimpleResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDetailResult {
    private UserResult userResult;
    private List<ReviewSimpleMyPageResult> latestReviews = new ArrayList<>();
    private List<UserStorageSimpleResult> favoriteSupplements = new ArrayList<>();
}
