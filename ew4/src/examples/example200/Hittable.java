/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.example200;

/**
 *
 * @author Daniele Paggi
 */
public interface Hittable {

    public void hit(float damagePower);
    public void recover(float recoverPower);
    public void die();

}
