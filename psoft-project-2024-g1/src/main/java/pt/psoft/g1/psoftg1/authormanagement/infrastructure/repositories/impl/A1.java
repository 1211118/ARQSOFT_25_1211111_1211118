package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import org.springframework.stereotype.Component;

import pt.psoft.g1.psoftg1.authormanagement.repositories.iA;

@Component
class A1 implements iA
{
    public void someFunction()
    {
        System.out.println("\n-----------[A1]:someFunction()\n");
    }
}