/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.util.common;

/**
 *
 * @author namnq
 */
public interface IZObjectBuilder<_Type> {

	_Type build(_Type o);
}
