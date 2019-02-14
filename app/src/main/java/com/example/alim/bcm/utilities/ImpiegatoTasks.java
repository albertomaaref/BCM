package com.example.alim.bcm.utilities;

import com.example.alim.bcm.data.model.Autista;

import java.util.List;

/**
 * Created by alim on 25-Mar-18.
 */

public interface ImpiegatoTasks {

    /**
     * @param objects attrezzi o materiali
     *
     */
    void comunicateObjectsToDriver(List<Object> objects, Autista autista);
}
