# HttpClient
针对自己项目对okhttp进行的封装，支持常规得get以及post请求


# sample
## 常规的get请求
```
public class MainActivity extends AppCompatActivity implements Observer{

    private static final String TAG = "RWJ MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonRequest request = new CommonRequest(Api.baseUrl);
                Map<String, String> map = request.getMap();
                map.put("name","盗墓笔记");
                HttpManager.getInstance().send(MainActivity.this,request,new Bean(),MainActivity.this);
            }
        });
    }

    @Override
    public void update(Observable o, BaseResponse response) {
        if (response instanceof Bean){
            Bean bean = (Bean) response;
            TLog.d(TAG,bean.toString());
        }
    }

```

## 常规得post请求
```
  CommonRequest request = new CommonRequest(Api.baseUrl, CommonRequest.HttpTypeEnum.POST);
  Map<String, String> map = request.getMap();
  map.put("name","盗墓笔记");
  HttpManager.getInstance().send(MainActivity.this,request,new Bean(),MainActivity.this);
```
只需要在创建CommonRequest的时候传入CommonRequest.HttpTypeEnum.POST即可，这种方式会把参数以key-velue得形势添加到body中

## json格式得post请求
```
  CommonRequest request = new CommonRequest(Api.baseUrl, CommonRequest.HttpTypeEnum.POST
                        , CommonRequest.ContentTypeEnum.APPLICATION_JSON);
  Map<String, String> map = request.getMap();
  map.put("name","盗墓笔记");
  HttpManager.getInstance().send(MainActivity.this,request,new Bean(),MainActivity.this);
```
这种方式得body是标准得json串，Content-type为application/json; charset=UTF-8
