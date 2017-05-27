//package com.dennyac.HbaseTest.api;
//
///**
// * Created by shuyun on 2017/3/28.
// */
//public class Around {
//
//    public Object around(ProceedingJoinPoint pjp)  {
////拦截的实体类
//        Object target = pjp.getTarget();
//        //拦截的方法名称
//        String methodName = pjp.getSignature().getName();
//        //拦截的放参数类型
//        Class[] parameterTypes = ((MethodSignature)pjp.getSignature()).getMethod().getParameterTypes();
//        Class[] clazzs = target.getClass().getInterfaces();
//        //1.获取类
//        Class clazz = target.getClass();
//        if (clazzs != null && clazzs.length > 0){
//            clazz = clazzs[0];
//        }
//        //2.获取方法
//        Method m = clazz.getMethod(methodName, parameterTypes);
//        //3.获取request、callback
//        Object[] args = pjp.getArgs();
//        HttpServletRequest request = null;
//        if (args != null && args.length > 0) {
//            if (args[0] instanceof HttpServletRequest) {
//                request = (HttpServletRequest) args[0];
//                if(request != null){
//                    callback = request.getParameter("callback");
//                }
//            }
//        }
//        //RequestLimit判断
//        String reequestLimitRes = this.RequestLimitCheck(m, request);
//        if("fail".equals(reequestLimitRes)){
//            return "fail";//返回值改为自己的格式
//        }else{
//            Object obj = pjp.proceed();
//        }
//    }
//
//    private String RequestLimitCheck(Method m, HttpServletRequest request) throws IOException{
//        //ip、user_phone+uri  两个维度的访问限制
//        if(m!=null && m.isAnnotationPresent(RequestLimit.class)){
//            RequestLimit requestLimit = m.getAnnotation(RequestLimit.class);
//            //失效时间、访问次数
//            int ipTime = (int) (requestLimit.ipTime() / 1000);
//            int ipCount = requestLimit.ipCount();
//            int uriTime = (int) (requestLimit.uriTime() / 1000);
//            int uriCount = requestLimit.uriCount();
//
//            //ip、user_phone、uri
//            String ip_key = NetworkUtil.getIpAddress(request);//ip:获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
//            String user_phone = request.getParameter("user_phone");//手机号
//            String uri = request.getRequestURI().toString();//uri:如果字符串太长,可以做个映射
//            String user_uri_key = user_phone + uri;
//            //先从缓存中(以Memcached为例)获取两个key对应的value值,value值为访问限制次数
//            List<String> keyCollections = new ArrayList<String>();
//            keyCollections.add(ip_key);
//            keyCollections.add(user_uri_key);
//            Map<String, String> valueMap = memcachedClient.get(keyCollections);//一次读取出多个key_value
//            Integer ipNumCache = 0;//ip访问次数
//            Integer userUriNumCache = 0;//手机号+uri访问次数
//            if(valueMap!=null && valueMap.size()>0){
//                String ipNumCacheFlag = valueMap.get(ip_key);
//                if(StringUtils.isNotBlank(ipNumCacheFlag)){
//                    ipNumCache = Integer.parseInt(ipNumCacheFlag);
//                }
//                String userUriNumCacheFlag = valueMap.get(user_uri_key);
//                if(StringUtils.isNotBlank(userUriNumCacheFlag)){
//                    userUriNumCache = Integer.parseInt(userUriNumCacheFlag);
//                }
//            }
//            //ip限制判断
//            if(ipNumCache == 0){
//                memcachedClient.set(ip_key, 1, ipTime);
//            }else if(ipNumCache >= ipCount){
//                logger.info("request_limit:用户IP[" + ip_key + "],超过了限定的次数[" + ipCount + "]");
//                return "fail";
//            }else{
//                memcachedClient.incr(ip_key, 1);//自增
//            }
//            //user_phone、uri限制判断
//            if(userUriNumCache == 0){
//                memcachedClient.set(user_uri_key, 1, uriTime);
//            }else if(userUriNumCache >= uriCount){
//                logger.info("request_limit:用户手机号[" + user_phone + "],访问地址[" + uri + "],超过了限定的次数[" + uriCount + "]");
//                return "fail";
//            }else{
//                memcachedClient.incr(user_uri_key, 1);
//            }
//        }
//        return "success";
//    }
//}
