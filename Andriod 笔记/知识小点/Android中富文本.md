##Android中富文本的实现

###SpannableStringBuilder

	//spannableString 实现spanable
	 SpannableString ss = new SpannableString("红色打电话斜体删除线绿色下划线图片:."); 
     //用颜色标记文本
     ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, 
             //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
             Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     //用超链接标记文本
     ss.setSpan(new URLSpan("tel:4155551212"), 2, 5, 
             Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     //用样式标记文本（斜体）
     ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 7, 
             Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     //用删除线标记文本
     ss.setSpan(new StrikethroughSpan(), 7, 10, 
             Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     //用下划线标记文本
     ss.setSpan(new UnderlineSpan(), 10, 16, 
             Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     //用颜色标记
     ss.setSpan(new ForegroundColorSpan(Color.GREEN), 10, 13, 
             Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     //获取Drawable资源
     Drawable d = getResources().getDrawable(R.drawable.icon); 
     d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
     //创建ImageSpan
     ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
     //用ImageSpan替换文本
     ss.setSpan(span, 18, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); 
     txtInfo.setText(ss);
     txtInfo.setMovementMethod(LinkMovementMethod.getInstance()); //实现文本的滚动

###WebView中可以使用HTML语言，HTML.fromHtml();

	TextView tv = new TextView(context);
	tv.setText(Html.fromHtml("<html>...</html>"));
	tv.setMovementMethod(LinkMovementMethod.getInstance());//设置可点击超链接方式
