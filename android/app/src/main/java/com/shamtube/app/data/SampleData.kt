package com.shamtube.app.data

import com.shamtube.app.data.local.ChannelEntity
import com.shamtube.app.data.local.CommentEntity
import com.shamtube.app.data.local.VideoEntity
import com.shamtube.app.data.model.Category

object SampleData {
    private const val avatarOne = "https://i.pravatar.cc/150?img=12"
    private const val avatarTwo = "https://i.pravatar.cc/150?img=32"
    private const val avatarThree = "https://i.pravatar.cc/150?img=49"

    val channels = listOf(
        ChannelEntity(
            id = "sham-today",
            name = "شام اليوم",
            avatarUrl = avatarOne,
            bannerUrl = "https://images.unsplash.com/photo-1539650116574-75c0c6d73f6e?w=1200",
            description = "أخبار وقصص من سوريا وبلاد الشام بصوت قريب من الناس.",
            subscribers = 184_000,
        ),
        ChannelEntity(
            id = "sham-music",
            name = "موسيقى الشام",
            avatarUrl = avatarTwo,
            bannerUrl = "https://images.unsplash.com/photo-1516280440614-37939bbacd81?w=1200",
            description = "جلسات موسيقية وأصوات سورية جديدة وقديمة.",
            subscribers = 92_500,
        ),
        ChannelEntity(
            id = "arab-sports",
            name = "ملاعب عربية",
            avatarUrl = avatarThree,
            bannerUrl = "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?w=1200",
            description = "كل ما يهمك من أخبار الرياضة العربية والعالمية.",
            subscribers = 76_800,
        ),
    )

    val videos = listOf(
        VideoEntity(
            id = "damascus-morning",
            title = "صباح دمشق | جولة هادئة بين الياسمين والأسواق القديمة",
            description = "نبدأ يومنا من قلب دمشق القديمة، بين الحارات التي تحفظ الحكايات ورائحة القهوة الصباحية.",
            thumbnailUrl = "https://images.unsplash.com/photo-1548013146-72479768bada?w=1000",
            videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            channelId = "sham-today",
            channelName = "شام اليوم",
            channelAvatar = avatarOne,
            views = 128_400,
            likes = 8_920,
            comments = 318,
            uploadDate = "منذ ساعتين",
            duration = "12:48",
            category = Category.NEWS.name,
            tags = "دمشق|سوريا|سياحة",
        ),
        VideoEntity(
            id = "sham-music-session",
            title = "جلسة عود تحت ضوء القمر | موسيقى من الذاكرة",
            description = "جلسة موسيقية دافئة تجمع العود والإيقاع في أجواء شامية هادئة.",
            thumbnailUrl = "https://images.unsplash.com/photo-1511379938547-c1f69419868d?w=1000",
            videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            channelId = "sham-music",
            channelName = "موسيقى الشام",
            channelAvatar = avatarTwo,
            views = 84_200,
            likes = 6_410,
            comments = 214,
            uploadDate = "منذ 5 ساعات",
            duration = "08:32",
            category = Category.MUSIC.name,
            tags = "عود|موسيقى|شام",
        ),
        VideoEntity(
            id = "arab-football-roundup",
            title = "ملخص الجولة: أجمل أهداف الأسبوع العربي",
            description = "نستعرض معكم أهم لقطات وأهداف الجولة في ملاعبنا العربية.",
            thumbnailUrl = "https://images.unsplash.com/photo-1579952363873-27f3bade9f55?w=1000",
            videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            channelId = "arab-sports",
            channelName = "ملاعب عربية",
            channelAvatar = avatarThree,
            views = 52_700,
            likes = 4_080,
            comments = 96,
            uploadDate = "أمس",
            duration = "06:14",
            category = Category.SPORTS.name,
            tags = "رياضة|كرة القدم|أهداف",
        ),
        VideoEntity(
            id = "learn-arabic-code",
            title = "كيف تبني أول تطبيق لك؟ شرح مبسّط للمبتدئين",
            description = "خطوات عملية لفهم فكرة التطبيقات وبناء أول مشروع صغير بطريقة سهلة.",
            thumbnailUrl = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=1000",
            videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            channelId = "sham-today",
            channelName = "شام اليوم",
            channelAvatar = avatarOne,
            views = 31_600,
            likes = 2_730,
            comments = 122,
            uploadDate = "منذ يومين",
            duration = "18:05",
            category = Category.EDUCATION.name,
            tags = "تعليم|تقنية|برمجة",
        ),
    )

    val comments = listOf(
        CommentEntity(
            id = "comment-1",
            videoId = "damascus-morning",
            userId = "user-1",
            userName = "ليان حمدان",
            userAvatar = "https://i.pravatar.cc/150?img=47",
            text = "يا سلام على الصباح الدمشقي، تصوير جميل جداً.",
            likes = 142,
            createdAt = "منذ 18 دقيقة",
        ),
        CommentEntity(
            id = "comment-2",
            videoId = "damascus-morning",
            userId = "user-2",
            userName = "سامر الشامي",
            userAvatar = "https://i.pravatar.cc/150?img=68",
            text = "ذكرتوني ببيت جدتي، شكراً على هذه الجولة.",
            likes = 68,
            createdAt = "منذ 42 دقيقة",
        ),
        CommentEntity(
            id = "comment-3",
            videoId = "sham-music-session",
            userId = "user-3",
            userName = "نور",
            userAvatar = "https://i.pravatar.cc/150?img=44",
            text = "الموسيقى واللقطة رائعان، ننتظر جلسات أكثر.",
            likes = 91,
            createdAt = "منذ ساعة",
        ),
    )
}