package com.demo;

public class WildCardMatchTest {

    /**
     * 双指针算法 - wild通配
     * <p>https://leetcode.cn/problems/wildcard-matching/solution/by-xuanmichael_cn-lmvf/</p>
     *
     * @param s
     * @param p
     * @return
     */
    public boolean isMatch(String s, String p) {
        int n = s.length(), m = p.length();
        boolean[][] dp = new boolean[n + 1][m + 1];
        dp[0][0] = true;
        char a, b;
        for (int i = 0; i <= n; ++i) {
            a = i == 0 ? ' ' : s.charAt(i - 1);
            for (int j = 1; j <= m; ++j) {
                b = p.charAt(j - 1);
                if (b == '*') {
                    dp[i][j] = dp[i][j - 1] || i > 0 && dp[i - 1][j];
                } else {
                    dp[i][j] = (a == b || b == '?') && i > 0 && dp[i - 1][j - 1];
                }
            }
        }
        return dp[n][m];
    }


    /**
     * 贪心算法 - wildcard匹配
     * <p>https://leetcode.cn/problems/wildcard-matching/solution/tong-pei-fu-pi-pei-by-leetcode-solution/</p>
     * @param s
     * @param p
     * @return
     */
    public boolean isMatch1(String s, String p) {
        int sRight = s.length(), pRight = p.length();
        while (sRight > 0 && pRight > 0 && p.charAt(pRight - 1) != '*') {
            if (charMatch(s.charAt(sRight - 1), p.charAt(pRight - 1))) {
                --sRight;
                --pRight;
            } else {
                return false;
            }
        }

        if (pRight == 0) {
            return sRight == 0;
        }

        int sIndex = 0, pIndex = 0;
        int sRecord = -1, pRecord = -1;

        while (sIndex < sRight && pIndex < pRight) {
            if (p.charAt(pIndex) == '*') {
                ++pIndex;
                sRecord = sIndex;
                pRecord = pIndex;
            } else if (charMatch(s.charAt(sIndex), p.charAt(pIndex))) {
                ++sIndex;
                ++pIndex;
            } else if (sRecord != -1 && sRecord + 1 < sRight) {
                ++sRecord;
                sIndex = sRecord;
                pIndex = pRecord;
            } else {
                return false;
            }
        }

        return allStars(p, pIndex, pRight);
    }

    public boolean allStars(String str, int left, int right) {
        for (int i = left; i < right; ++i) {
            if (str.charAt(i) != '*') {
                return false;
            }
        }
        return true;
    }

    public boolean charMatch(char u, char v) {
        return u == v || v == '?';
    }


}
