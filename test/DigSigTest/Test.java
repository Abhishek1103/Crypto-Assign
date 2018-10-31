/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DigSigTest;

/**
 *
 * @author aks
 */
public class Test {
    
    int a;
    
    Test(int a){
        this.a = a;
    }
    
    public static void main(String[] args) {
        int[] arr = {1,2,3};
        
        Test t = new Test(5);
        int[] q = t.doit(arr);
        System.out.println("arr[1]: "+arr[1]);
        System.out.println("q[1]: "+q[1]);
        
        t.doiti(t);
        System.out.println("t.a: "+t.a);
    }
    
    
    public int[] doit(int[] arr){
        arr[1] = 0;
        return arr;
    }
    
    public void doiti(Test t){
        t.a = 10;
    }
}
