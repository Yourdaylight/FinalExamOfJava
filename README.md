# Java大作业（文件传输系统）
##功能
    2.1发送消息
    客户端和服务端建立了Tcp连接，当用户需要发送消息的时候，只需要把文字输入在客户端文本框，然后点击按钮“发送消息”，服务器端即可收到客户端发送的内容。
    2.2文件传输
    用户在客户端点击“上传文件”按钮，会弹出来一个计算机的文件选择系统供用户选择，用户点击想要上传的文件，点击按钮“打开”，即可上传至服务器端，服务器端会收到此文件，可自由选择保存此文件的目录，若此文件为Excel文件或是储存类似Excel数据的txt文件，则会保存至数据库，期间用户也会收到来自系统的消息提示，例如：“文件已发送完毕”、“文件已接收完毕”、“开始写入数据库”、“写入数据库成功”等通知。若用户上传相同的Excel文件，则在存入数据库的时候在文件名前面加入随机前缀加以区分。
    2.3下载文件
    用户在客户端点击“下载文件”按钮，系统会弹出一个文件选择的界面，点击“浏览”即可自由选择下载数据库中的文件，并可选择将此文件导出为Excel文件或者txt文件，用户可自由选择文件保存的目录。
    2.4管理文件
    用户在(客户端/服务器端)上传文件/下载文件后,客户端/服务器端可点击”管理接受文件”按钮,系统会弹出来一个管理文件的界面,即可对于客户端/服务器端接受到的文件的删除，复制，剪切操作，也可实现对数据库表的增删改以及查看。
