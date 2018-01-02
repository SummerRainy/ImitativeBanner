package com.example.gongxuan.banner.bean
/**
 * Created by gongxuan on 2017/7/5.
 */

class NewsRecommendList {

    var recommend_list: ArrayList<RecommendListBean>? = null

    class RecommendListBean {
        /**
         * pid : 15519550
         * tid : 1481668
         * subject : 常州"女神"老师毕业典礼上"最后一课"刷爆朋友圈
         * description : 常州"女神"老师毕业典礼上"最后一课"刷爆朋友圈 图
         * type : 1
         * dateline : 07-05 09:07
         * time_past : 7小时前
         * comment_counts : 0
         * support_counts : 0
         * against_counts : 0
         * shares_weibo : 0
         * shares_weixin : 0
         * hits : 31
         * thumb_pic : {"url":"http://cznews.cz001.com.cn/attachment/pic/201707/05/595c3c2eed780.jpg","width":600,"height":400}
         */

        var pid: Int = 0
        var tid: Int = 0
        var subject: String? = null
        var description: String? = null
        var type: Int = 0
        var dateline: String? = null
        var time_past: String? = null
        var comment_counts: Int = 0
        var support_counts: Int = 0
        var against_counts: Int = 0
        var shares_weibo: Int = 0
        var shares_weixin: Int = 0
        var hits: Int = 0
        var thumb_pic: ThumbPicBean? = null

        class ThumbPicBean {
            /**
             * url : http://cznews.cz001.com.cn/attachment/pic/201707/05/595c3c2eed780.jpg
             * width : 600
             * height : 400
             */

            var url: String? = null
            var width: Int = 0
            var height: Int = 0
        }
    }
}
