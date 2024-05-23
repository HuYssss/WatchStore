clear all
global M2 Jy M1t M1s K1t K1s K2t K2s C1t C1s C2t C2s a b q0 v L w l t phi aZ MM kq G q1 q2 q3 q4
kq = [];
%KHOI LUONG
M2 = 14400;   %Khoi luong duoc treo (kg)
M1t = 472;    %Khoi luong khong treo cau truoc (kg)
M1s = 828;    %Khoi luong khong treo cau sau (kg)
Jy = 55354;   %Momen quan tinh khoi luong quanh Oy (kg.m^2)
%HE SO CAN
K1t = 32765;  %He so can lop truoc (Ns/m)
K1s = 48730;  %He so can lop sau (Ns/m)
K2t = 17431;  %He so can giam chan truoc(Ns/m)
K2s = 26708;  %He so can giam chan sau (Ns/m)
%DO CUNG
C1t = 899472; %Do cung banh xe truoc (N/m)
C1s = 1578020;%Do cung banh xe sau (N/m)
C2t = 363177; %Do cung HTT truoc (N/m)
C2s = 486111; %Do cung HTT sau (N/m)
l = 5.4;      %Chieu dai co so (m)
q0 = 0.06;    %Chieu cao map mo (m)
L = 2;        %Chieu dai map mo (m)
v = 12;       %Toc do khao sat (m/s)
w = (2*pi*v)/L;
phi = 2*pi*l/L;
dt = 0.01;    %Buoc nhay thoi gian (s)
T = 10;       %Thoi gian khao sat (s)
nt = fix(T/dt);
aZ = zeros(4, nt+1);
vZ = zeros(4, nt+1);
Z = zeros(4, nt+1);
for a = 0:dt:5.4;
    b = l - a;  
    %Ma tran M
    M = [M2 0 0 0; 0 Jy 0 0; 0 0 M1t 0; 0 0 0 M1s];
    MM = inv(M);
    %Ma tran K
    K = [K2t+K2s b*K2s-a*K2t -K2t -K2s; b*K2s-a*K2t a*a*K2t+b*b*K2s a*K2t -b*K2s; -K2t a*K2t K1t+K2t 0; -K2s -b*K2s 0 K1s+K2s];
    %Ma tran C
    C = [C2t+C2s b*C2s-a*C2t -C2t -C2s; b*C2s-a*C2t a*a*C2t+b*b*C2s a*C2t -b*C2s; -C2t a*C2t C1t+C2t 0; -C2s -b*C2s 0 C1s+C2s];
    vZ(:,1) = [0 0 0 0];
    Z(:,1) = [0 0 0 0];
    for t = 1:nt
        if t >= 0 && t <= (2*L/v)
            q1 = 1 - cos(w*t);
            q2 = sin(w*t);
            if t >= l/v && t <= (2*L/v) + (l/v)
                q3 = 1 - cos(w*t + phi);
                q4 = sin(w*t+ phi);
        else
            q1 = 0;
            q2 = 0;
            q3 = 0;
            q4 = 0;
        end
        f = [0; 0; 0.5*C1t*q0*q1 + 0.5*K1t*q0*w*q2; 0.5*C1s*q0*q3 + 0.5*K1s*q0*w*q4];
        aZ(:,t) = MM * (f - K*vZ(:,t) - C*Z(:,t));
        vZ(:,t+1) = vZ(:,t) + aZ(:,t) * dt;
        Z(:,t+1) = Z(:,t) + vZ(:,t+1) * dt;
        end
    end
    %Trong so gia toc RMS
    S = 0;
    for t = 1:nt
        S = S + aZ(1,t)^2*dt;
    end
    S = sqrt(S/T);  
    kq = [kq S];
end
G = kq;
a = 0:0.01:5.4;
figure(1)
plot(a, G, 'r', 'LineWidth', 1.3)
xlabel('Vi tri em diu (m)')
ylabel('RMS (m/s^2)')
grid on