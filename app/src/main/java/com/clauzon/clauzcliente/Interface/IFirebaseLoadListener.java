package com.clauzon.clauzcliente.Interface;

import com.clauzon.clauzcliente.Clases.Category;

import java.util.List;

public interface IFirebaseLoadListener {

    void OnFirebaseLoadSuccess(List<Category> grupo_categorias);
    void OnFirebaseFaield(String message);
}
