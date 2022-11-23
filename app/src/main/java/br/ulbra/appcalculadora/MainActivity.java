package br.ulbra.appcalculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button numeroZero, numeroUm, numeroDois, numeroTres,
            numeroQuatro, numeroCinco, numeroSeis, numeroSete,
            numeroOito, numeroNove, ponto, divisao, multiplicacao,
            soma, subtracao, igual, botao_limpar, porcentagem, potencia;
    private TextView txtExpressao, txtResultado;
    private ImageView backspace;
    String operacaoAtual;
    String numeroAtual = "";
    boolean equalWasPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        iniciarComponentes();

        numeroZero.setOnClickListener(this);
        numeroUm.setOnClickListener(this);
        numeroUm.setOnClickListener(this);
        numeroDois.setOnClickListener(this);
        numeroTres.setOnClickListener(this);
        numeroQuatro.setOnClickListener(this);
        numeroCinco.setOnClickListener(this);
        numeroSeis.setOnClickListener(this);
        numeroSete.setOnClickListener(this);
        numeroOito.setOnClickListener(this);
        numeroNove.setOnClickListener(this);
        ponto.setOnClickListener(this);
        soma.setOnClickListener(this);
        subtracao.setOnClickListener(this);
        multiplicacao.setOnClickListener(this);
        divisao.setOnClickListener(this);
        potencia.setOnClickListener(this);

        botao_limpar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtExpressao.setText("");
                        txtResultado.setText("");
                        numeroAtual = "";
                    }
                }
        );

        porcentagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtExpressao.getText().equals("")) {
                    Toast.makeText(MainActivity.this, "Informe o valor xiru", Toast.LENGTH_SHORT).show();
                } else {
                    DecimalFormat f = new DecimalFormat("0.00");

                    String regex = operacaoAtual + numeroAtual;

                    double numeroAnterior = Double.parseDouble(String.valueOf(txtExpressao.getText().toString().replace(regex, "")));
                    double numeroDecimal =  (Double.parseDouble(numeroAtual) / 100.0) * numeroAnterior;
                    String contaCompleta = numeroAnterior + " " + operacaoAtual + " " + numeroDecimal;

                    try {
                        Expression expressao = new ExpressionBuilder(contaCompleta).build();
                        double resultado = expressao.evaluate();
                        long longResult = (long) resultado;
                        if (resultado == (double) longResult) {
                            txtResultado.setText((CharSequence) String.valueOf(longResult));
                            txtExpressao.setText(String.valueOf(numeroAnterior) + " " + operacaoAtual + " " + String.valueOf(f.format(numeroDecimal)) + " =");
                            equalWasPressed = true;
                        } else {
                            txtResultado.setText((CharSequence) String.valueOf(resultado));
                            txtExpressao.setText(String.valueOf(numeroAnterior) + " " + operacaoAtual + " " + String.valueOf(f.format(numeroDecimal)) + " =");
                            equalWasPressed = true;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView expressao = findViewById(R.id.txt_expressao);
                String string = expressao.getText().toString();
                if (!string.isEmpty()) {
                    byte var0 = 0;
                    int var1 = string.length() - 1;
                    String txtExpressao = string.substring(var0, var1);
                    expressao.setText(txtExpressao);
                }
                txtResultado.setText("");
            }
        });

        igual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Expression expressao = new ExpressionBuilder(txtExpressao.getText().toString()).build();
                    double resultado = expressao.evaluate();
                    long longResult = (long) resultado;
                    if (resultado == (double) longResult) {
                        txtResultado.setText((CharSequence) String.valueOf(longResult));
                        txtExpressao.append(" = ");
                        equalWasPressed = true;
                    } else {
                        txtResultado.setText((CharSequence) String.valueOf(resultado));
                        txtExpressao.append(" = ");
                        equalWasPressed = true;
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private void iniciarComponentes() {
        numeroZero = findViewById(R.id.numero_zero);
        numeroUm = findViewById(R.id.numero_um);
        numeroDois = findViewById(R.id.numero_dois);
        numeroTres = findViewById(R.id.numero_tres);
        numeroQuatro = findViewById(R.id.numero_quatro);
        numeroCinco = findViewById(R.id.numero_cinco);
        numeroSeis = findViewById(R.id.numero_seis);
        numeroSete = findViewById(R.id.numero_sete);
        numeroOito = findViewById(R.id.numero_oito);
        numeroNove = findViewById(R.id.numero_nove);

        ponto = findViewById(R.id.ponto);
        soma = findViewById(R.id.adicao);
        subtracao = findViewById(R.id.subtracao);
        multiplicacao = findViewById(R.id.multiplicacao);
        divisao = findViewById(R.id.divisao);
        potencia = findViewById(R.id.bt_potencia);
        porcentagem = findViewById(R.id.bt_porcentagem);
        igual = findViewById(R.id.igual);
        potencia = findViewById(R.id.bt_potencia);

        botao_limpar = findViewById(R.id.bt_limpar);
        txtExpressao = findViewById(R.id.txt_expressao);
        txtResultado = findViewById(R.id.txt_resultado);
        backspace = findViewById(R.id.backspace);
    }

    public void acrescentarUmaExpressao(String string, boolean limpar_dados, boolean operacao) {
        if(!operacao){
            numeroAtual += string;
        }

        if (limpar_dados && !operacao) {
            txtExpressao.append(string);
            txtResultado.setText(numeroAtual);
        } else if (!operacao){
            txtExpressao.append(txtResultado.getText());
            txtExpressao.append(string);
            txtResultado.setText(" ");
        } else {
            txtExpressao.append(string);
            numeroAtual = "";
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.numero_zero:
                acrescentarUmaExpressao("0", true, false);
                break;
            case R.id.numero_um:
                acrescentarUmaExpressao("1", true, false);
                break;
            case R.id.numero_dois:
                acrescentarUmaExpressao("2", true, false);
                break;
            case R.id.numero_tres:
                acrescentarUmaExpressao("3", true, false);
                break;
            case R.id.numero_quatro:
                acrescentarUmaExpressao("4", true, false);
                break;
            case R.id.numero_cinco:
                acrescentarUmaExpressao("5", true, false);
                break;
            case R.id.numero_seis:
                acrescentarUmaExpressao("6", true, false);
                break;
            case R.id.numero_sete:
                acrescentarUmaExpressao("7", true, false);
                break;
            case R.id.numero_oito:
                acrescentarUmaExpressao("8", true, false);
                break;
            case R.id.numero_nove:
                acrescentarUmaExpressao("9", true, false);
                break;
            case R.id.adicao:
                operacaoAtual = "+";
                prepararProximaOperacao();
                acrescentarUmaExpressao("+", true, true);
                break;
            case R.id.subtracao:
                operacaoAtual = "-";
                prepararProximaOperacao();
                acrescentarUmaExpressao("-", true, true);
                break;
            case R.id.multiplicacao:
                operacaoAtual = "*";
                prepararProximaOperacao();
                acrescentarUmaExpressao("*", true, true);
                break;
            case R.id.divisao:
                operacaoAtual = "/";
                prepararProximaOperacao();
                acrescentarUmaExpressao("/", true, true);
                break;
            case R.id.ponto:
                acrescentarUmaExpressao(".", true, false);
                break;
            case R.id.bt_potencia:
                operacaoAtual = "^";
                prepararProximaOperacao();
                acrescentarUmaExpressao("^", true, true);
                break;
        }
    }

    public void prepararProximaOperacao(){
        if(equalWasPressed) {
            txtExpressao.setText(txtResultado.getText().toString());
        }
    }
}