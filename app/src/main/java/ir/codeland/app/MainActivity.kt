package ir.codeland.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaquo.python.Python
import org.json.JSONObject

private val blue = Color(0xFF4778D8)
private val navy = Color(0xFF101C32)
private data class Lesson(val title: String, val summary: String, val text: String, val code: String)
private val lessons = listOf(
 Lesson("اولین برنامه پایتون","آشنایی با print","تابع print برای نمایش متن و مقدارها در خروجی استفاده می‌شود.","print(\"سلام کد‌لند!\")"),
 Lesson("متغیرها","ذخیره اطلاعات","متغیر نامی برای نگهداری یک مقدار است. پایتون نوع داده را از روی مقدار تشخیص می‌دهد.","name = \"Matin\"\nage = 16\nprint(name)\nprint(age)"),
 Lesson("محاسبات","عملگرهای پایه","از + برای جمع، - برای تفریق، * برای ضرب و / برای تقسیم استفاده می‌کنیم.","a = 12\nb = 5\nprint(a + b)\nprint(a * b)"),
 Lesson("شرط‌ها","تصمیم‌گیری با if","با if اجرای بخشی از برنامه را به برقرار بودن یک شرط وابسته می‌کنیم. تورفتگی مهم است.","score = 18\nif score >= 10:\n    print(\"قبول شدی\")\nelse:\n    print(\"بیشتر تمرین کن\")"),
 Lesson("حلقه for","تکرار دستورها","حلقه for دستورها را برای اعضای یک مجموعه یا بازه تکرار می‌کند.","for n in range(1, 6):\n    print(n)")
)
class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  try { if (!Python.isStarted()) Python.start(com.chaquo.python.android.AndroidPlatform(this)) } catch (_: Exception) {}
  setContent { CodeLand() }
 }
}
@Composable private fun CodeLand() {
 var page by remember { mutableStateOf("home") }
 var chosen by remember { mutableIntStateOf(0) }
 var code by remember { mutableStateOf(lessons[0].code) }
 var output by remember { mutableStateOf("کدت را بنویس و اجرا کن.") }
 var done by remember { mutableStateOf(setOf<Int>()) }
 var dark by remember { mutableStateOf(false) }
 val scheme = if (dark) darkColorScheme(primary = Color(0xFF8EB4FF), background = navy, surface = Color(0xFF1B2A43)) else lightColorScheme(primary = blue, background = Color(0xFFF3F6FB))
 MaterialTheme(colorScheme = scheme) {
  Scaffold(bottomBar = { NavigationBar {
   NavigationBarItem(page=="home",{page="home"},{Icon(Icons.Default.Home,null)},label={Text("خانه")})
   NavigationBarItem(page=="learn",{page="learn"},{Icon(Icons.Default.MenuBook,null)},label={Text("یادگیری")})
   NavigationBarItem(page=="editor",{page="editor"},{Icon(Icons.Default.Code,null)},label={Text("محیط کد")})
   NavigationBarItem(page=="profile",{page="profile"},{Icon(Icons.Default.Person,null)},label={Text("پروفایل")})
  } }) { pad ->
   when(page) {
    "home" -> Column(Modifier.fillMaxSize().padding(pad).padding(20.dp), verticalArrangement=Arrangement.spacedBy(18.dp)) {
     Text("CODELAND", color=blue, fontWeight=FontWeight.Bold, letterSpacing=3.sp)
     Text("برنامه‌نویسی را\nاز همین‌جا شروع کن", fontSize=27.sp, fontWeight=FontWeight.Bold)
     Card(colors=CardDefaults.cardColors(containerColor=navy), shape=RoundedCornerShape(22.dp)) {
      Column(Modifier.fillMaxWidth().padding(20.dp)) {
       Text("مسیر یادگیری پایتون", color=Color.White, fontSize=19.sp, fontWeight=FontWeight.Bold)
       Text("${done.size} از ${lessons.size} درس تکمیل شده", color=Color.LightGray)
       LinearProgressIndicator(progress={done.size.toFloat()/lessons.size}, modifier=Modifier.fillMaxWidth().padding(vertical=12.dp))
       Button(onClick={page="learn"}, modifier=Modifier.fillMaxWidth()) { Text("ادامه یادگیری") }
      }
     }
     OutlinedButton(onClick={page="editor"}, modifier=Modifier.fillMaxWidth()) { Icon(Icons.Default.Terminal,null); Text("  ورود به محیط کدنویسی") }
     Text("آموزش فارسی · رایگان · آنلاین و آفلاین", color=Color.Gray)
    }
    "learn" -> LazyColumn(Modifier.fillMaxSize().padding(pad).padding(18.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
     item { Text("مسیر یادگیری پایتون", fontSize=24.sp, fontWeight=FontWeight.Bold) }
     items(lessons.indices.toList()) { i ->
      Card(Modifier.fillMaxWidth().clickable { chosen=i; code=lessons[i].code; page="lesson" }) {
       Row(Modifier.padding(16.dp), verticalAlignment=Alignment.CenterVertically) {
        Text("${i+1}", color=blue, fontWeight=FontWeight.Bold)
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) { Text(lessons[i].title,fontWeight=FontWeight.Bold); Text(lessons[i].summary,color=Color.Gray,fontSize=12.sp) }
        Icon(if(i in done) Icons.Default.CheckCircle else Icons.Default.PlayCircle,null,tint=blue)
       }
      }
     }
    }
    "lesson" -> Column(Modifier.fillMaxSize().padding(pad).padding(20.dp)) {
     Text(lessons[chosen].title,fontSize=25.sp,fontWeight=FontWeight.Bold)
     Text(lessons[chosen].summary,color=blue)
     Spacer(Modifier.height(18.dp))
     Card { Text(lessons[chosen].text,Modifier.padding(18.dp),fontSize=16.sp,lineHeight=27.sp) }
     Spacer(Modifier.height(18.dp))
     Text("نمونه کد",fontWeight=FontWeight.Bold)
     Card(colors=CardDefaults.cardColors(containerColor=navy)) { Text(lessons[chosen].code,Modifier.fillMaxWidth().padding(16.dp),color=Color(0xFFD8E5F7),fontFamily=FontFamily.Monospace) }
     Spacer(Modifier.weight(1f))
     Button(onClick={page="editor"},modifier=Modifier.fillMaxWidth()) { Text("تمرین در ویرایشگر") }
     TextButton(onClick={done=done+chosen;page="learn"},modifier=Modifier.align(Alignment.CenterHorizontally)) { Text("تکمیل درس") }
    }
    "editor" -> Column(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
     Text("محیط کدنویسی",fontSize=24.sp,fontWeight=FontWeight.Bold)
     Text("Python 3.11 · اجرای محلی",color=Color.Gray)
     OutlinedTextField(code,{code=it},modifier=Modifier.weight(1f).fillMaxWidth().padding(top=12.dp),label={Text("کد پایتون")},textStyle=androidx.compose.ui.text.TextStyle(fontFamily=FontFamily.Monospace,fontSize=14.sp))
     Button(onClick={output=executePython(code)},modifier=Modifier.fillMaxWidth().padding(top=10.dp)) { Icon(Icons.Default.PlayArrow,null); Text("  اجرای کد") }
     Text("خروجی",fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=12.dp))
     Card(Modifier.fillMaxWidth().heightIn(min=90.dp),colors=CardDefaults.cardColors(containerColor=navy)) { Text(output,Modifier.padding(14.dp),color=Color(0xFFD8E5F7)) }
    }
    else -> Column(Modifier.fillMaxSize().padding(pad).padding(20.dp)) {
     Text("پروفایل یادگیری",fontSize=24.sp,fontWeight=FontWeight.Bold)
     Text("${done.size} درس تکمیل‌شده")
     Text("امتیاز: ${done.size*20} XP",color=blue,fontWeight=FontWeight.Bold)
     Row(verticalAlignment=Alignment.CenterVertically) { Text("حالت تاریک",Modifier.weight(1f)); Switch(dark,{dark=it}) }
     Text("CodeLand 0.1.0",color=Color.Gray)
    }
   }
  }
 }
}
private fun executePython(source:String):String = try {
 val result=Python.getInstance().getModule("runner").callAttr("run",source).toString()
 val json=JSONObject(result)
 val out=json.optString("output")
 if(json.optBoolean("ok")) out.ifBlank { "برنامه بدون خطا اجرا شد؛ خروجی‌ای تولید نشد." } else out+json.optString("error")
} catch(e:Exception) { "مفسر پایتون آماده نیست: ${e.message}" }
