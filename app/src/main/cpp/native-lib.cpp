#include <jni.h>
#include <string>
#include "tinyxml2.h"

using namespace tinyxml2;

extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_trung_rssreaderndk_MainActivity_parseXml(JNIEnv *env, jobject instance,
                                                          jstring xml_) {
    const char *xml = env->GetStringUTFChars(xml_, 0);
    static jclass java_util_ArrayList = env->FindClass("java/util/ArrayList");
    jmethodID java_util_ArrayList_Constructor = env->GetMethodID(java_util_ArrayList, "<init>", "()V");
    jmethodID java_util_ArrayList_add = env->GetMethodID(java_util_ArrayList, "add", "(Ljava/lang/Object;)Z");
    jobject newsList = env->NewObject(java_util_ArrayList, java_util_ArrayList_Constructor);
    jclass cls_NewsData = env->FindClass("com/example/trung/rssreaderndk/NewsData");
    jmethodID NewsData_Constructor = env->GetMethodID(cls_NewsData, "<init>",
                                                      "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

    XMLDocument doc;
    doc.Parse(xml);
    XMLElement *rssElement = doc.FirstChildElement( "rss" );
    if(rssElement == NULL) return NULL;
    int i = 0;
    XMLElement *itemElement = rssElement->FirstChildElement( "channel" )->FirstChildElement( "item" );
    while (itemElement != NULL && i < 10) {
        XMLElement *test = itemElement->FirstChildElement("pubDate");
        if (test != NULL) {
            const char* title = itemElement->FirstChildElement("title")->GetText();
            const char* description = itemElement->FirstChildElement("description")->GetText();
            const char* pubDate = itemElement->FirstChildElement("pubDate")->GetText();
            char* str = NULL;
            description = strstr(description, "src");
            description +=5;
            str = strstr(description, "\"");
            str[0] = 0;
            jobject news = env->NewObject(cls_NewsData, NewsData_Constructor, env->NewStringUTF(description),
                                          env->NewStringUTF(title), env->NewStringUTF(pubDate));
            env->CallBooleanMethod(newsList, java_util_ArrayList_add, news);
        }
        itemElement = itemElement->NextSiblingElement("item");
        i++;
    }
    return newsList;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_trung_rssreaderndk_MainActivity_testParseXml(JNIEnv *env, jobject instance,
                                                          jstring xml_) {
    const char *xml = env->GetStringUTFChars(xml_, 0);
    XMLDocument doc;
    doc.Parse(xml);
    XMLElement *rssElement = doc.FirstChildElement( "rss" );
    if(rssElement == NULL) {
        printf("error");
        return 0;
    }
    XMLElement *firstItemElement = rssElement->FirstChildElement( "channel" )->FirstChildElement( "item" );
    const char* title = firstItemElement->FirstChildElement("title")->GetText();
    const char* description = firstItemElement->FirstChildElement("description")->GetText();
    const char* pubDate = firstItemElement->FirstChildElement("pubDate")->GetText();
    char* str = NULL;
    description = strstr(description, "src");
    description +=5;
    str = strstr(description, "\"");
    str[0] = 0;

    jclass cls = env->FindClass("com/example/trung/rssreaderndk/NewsData");
    jmethodID methodID = env->GetMethodID(cls, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    jobject obj = env->NewObject(cls, methodID, env->NewStringUTF(description),
                                 env->NewStringUTF(title), env->NewStringUTF(pubDate));
    return obj;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_trung_rssreaderndk_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
