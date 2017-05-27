package com.dennyac.HbaseTest.thread;

/**
 * Created by shuyun on 2016/9/21.
 *
 * 问题介绍

   生产者消费者模型是经典的同步问题。问题大致如下：当队列满时，生产者需要等待队列有空间才能继续往里面放入商品，而在等待的期间内，生产者必须释放对临界资源（即队列）的占用权。因为生产者如果不释放对临界资源的占用权，那么消费者就无法消费队列中的商品，就不会让队列有空间，那么生产者就会一直无限等待下去。因此，一般情况下，当队列满时，会让生产者交出对临界资源的占用权，并进入挂起状态。然后等待消费者消费了商品，然后消费者通知生产者队列有空间了。同样地，当队列空时，消费者也必须等待，等待生产者通知它队列中有商品了。这种互相通信的过程就是线程间的协作。

 下面总结了三种方式来实现一个消费者/生产者模型.
 */
public class CustomerTest {

    //1.wait/notify模式
    /**
     * wait and notify
     * 生产者和消费者问题
     *
     */

    public static void main(String[] args) {
        Resource resource = new Resource();
        //生产者线程
        ProducerThread p = new ProducerThread(resource);
        //多个消费者
        ConsumerThread c1 = new ConsumerThread(resource);
        ConsumerThread c2 = new ConsumerThread(resource);
        ConsumerThread c3 = new ConsumerThread(resource);

        p.start();
        c1.start();
        c2.start();
        c3.start();
    }

}


class ProducerThread extends Thread {

    private Resource resource;

    public ProducerThread(Resource resource) {
        this.resource = resource;
        //setName("生产者");
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (1000 * Math.random()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.add();
        }
    }
}

class ConsumerThread extends Thread {
    private Resource resource;

    public ConsumerThread(Resource resource) {
        this.resource = resource;
        //setName("消费者");
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (1000 * Math.random()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.remove();
        }
    }
}


/**
 * 公共资源类
 */
class Resource {

    //当前资源数量
    private int num = 0;
    //资源池中允许存放的资源数
    private int size = 10;

    /***
     * 向资源池中添加资源
     */
    public synchronized void add() {

        if (num < size) {
            num++;
            System.out.println(Thread.currentThread().getName() + "生产一件资源,当前资源池有" + num + "个");
            notifyAll();
        } else {
            try {
                wait();
                System.out.println(Thread.currentThread().getName() + "线程进入等待");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从资源池中取走资源
     */
    public synchronized void remove() {

        if (num > 0) {
            num--;
            System.out.println("消费者" + Thread.currentThread().getName() + "消耗一件资源," + "当前资源池有" + num + "个");
            notifyAll();
        } else {
            try {
                wait();
                System.out.println(Thread.currentThread().getName() + "线程进入等待");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

//2、Lock condition模式

